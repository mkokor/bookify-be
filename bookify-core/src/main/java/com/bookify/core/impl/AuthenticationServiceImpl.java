package com.bookify.core.impl;

import com.bookify.api.AuthenticationService;
import com.bookify.api.EmailService;
import com.bookify.api.TokenService;
import com.bookify.api.enums.ConfirmationCodeType;
import com.bookify.api.enums.EmailSubject;
import com.bookify.api.enums.Role;
import com.bookify.api.model.GenericResponse;
import com.bookify.api.model.authentication.AccessRefreshRequest;
import com.bookify.api.model.authentication.PasswordResetCodeRequest;
import com.bookify.api.model.authentication.PasswordResetRequest;
import com.bookify.api.model.authentication.UserLoginRequest;
import com.bookify.api.model.authentication.UserLoginResponse;
import com.bookify.api.model.book.BookResponse;
import com.bookify.api.model.confirmationcode.EmailConfirmationCodeUpdateRequest;
import com.bookify.api.model.confirmationcode.EmailConfirmationRequest;
import com.bookify.api.model.exception.ApiException;
import com.bookify.api.model.exception.BadRequestException;
import com.bookify.api.model.exception.InternalServerException;
import com.bookify.api.model.exception.NotFoundException;
import com.bookify.api.model.exception.UnauthenticatedException;
import com.bookify.api.model.exception.UnauthorizedException;
import com.bookify.api.model.user.UserRegistrationRequest;
import com.bookify.api.model.user.UserResponse;
import com.bookify.core.mapper.UserMapper;
import com.bookify.dao.model.*;
import com.bookify.dao.repository.ConfirmationCodeRepository;
import com.bookify.dao.repository.RefreshTokenRepository;
import com.bookify.dao.repository.RoleRepository;
import com.bookify.dao.repository.UserRepository;
import jakarta.transaction.Transactional;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final RefreshTokenRepository refreshTokenRepository;
  private final ConfirmationCodeRepository confirmationCodeRepository;
  private final UserMapper userMapper;
  private final TokenService tokenService;
  private final EmailService emailService;
  private final PasswordEncoder encoder;
  private final AuthenticationManager authenticationManager;

  public static final String USERNAME = "username";
  public static final String EMAIL = "email";

  @Value("${email.confirmation.code.expiration.minutes}")
  private int emailConfirmationCodeExpirationMinutes;

  @Value("${email.confirmation.code.length}")
  private int emailConfirmationCodeLength;

  @Value("${password.reset.code.expiration.minutes}")
  private int passwordResetCodeExpirationMinutes;

  @Value("${password.reset.code.length}")
  private int passwordResetCodeLength;

  @Value("${refresh.token.expiration.minutes}")
  private int refreshTokenExpirationMinutes;

  @Override
  @Transactional
  public UserResponse register(final UserRegistrationRequest userRegistrationRequest)
      throws ApiException {
    validateUniqueFieldConstraint(userRepository::existsByUsername,
        userRegistrationRequest.getUsername(), USERNAME);
    validateUniqueFieldConstraint(userRepository::existsByEmail,
        userRegistrationRequest.getEmail(), EMAIL);

    final RoleEntity customerRole = roleRepository.findByName(Role.CUSTOMER.getValue())
        .orElseThrow(() -> new InternalServerException("Something went wrong."));

    final UserEntity userEntity = userMapper.toEntity(userRegistrationRequest);
    userEntity.setPassword(encoder.encode(userRegistrationRequest.getPassword()));
    userEntity.setRoles(Set.of(customerRole));
    userEntity.setEmailConfirmed(Boolean.FALSE);
    userEntity.setLocked(Boolean.FALSE);

    userRepository.save(userEntity);

    final String confirmationCode = createEmailConfirmationCode(userEntity);

    sendEmailConfirmationCode(userEntity, confirmationCode);

    return userMapper.toUserResponse(userEntity);
  }

  @Override
  @Transactional
  public GenericResponse resendEmailConfirmationCode(
      final EmailConfirmationCodeUpdateRequest emailConfirmationCodeUpdateRequest)
      throws ApiException {
    final UserEntity userEntity = findUserByEmail(
        emailConfirmationCodeUpdateRequest.getUserEmail());

    if (CollectionUtils.containsInstance(userEntity.getRoles(), Role.CUSTOMER.getValue())) {
      throw new BadRequestException(
          "Email confirmation code could not be sent for a non-customer account using this API.");
    }
    if (userEntity.isEnabled()) {
      throw new BadRequestException("User email address is already confirmed.");
    }

    deleteExistingEmailConfirmationCode(userEntity);
    final String confirmationCode = createEmailConfirmationCode(userEntity);

    sendEmailConfirmationCode(userEntity, confirmationCode);

    final GenericResponse response = new GenericResponse();
    response.setMessage(String.format("The email confirmation code is successfully sent to %s.",
        userEntity.getEmail()));

    return response;
  }

  @Override
  @Transactional
  public UserResponse confirmEmailAddress(final EmailConfirmationRequest emailConfirmationRequest)
      throws ApiException {
    final UserEntity userEntity = findUserByEmail(emailConfirmationRequest.getUserEmail());

    if (Boolean.TRUE.equals(userEntity.getEmailConfirmed())) {
      throw new BadRequestException("Provided email address was already confirmed.");
    }

    final ConfirmationCodeEntity confirmationCodeEntity = confirmationCodeRepository.findByUserAndType(
            userEntity, ConfirmationCodeType.EMAIL_CONFIRMATION.getValue())
        .orElseThrow(() -> new BadRequestException(
            "Email confirmation code is not generated for the account with the provided email."));

    validateConfirmationCode(emailConfirmationRequest.getConfirmationCode(),
        confirmationCodeEntity);
    confirmationCodeRepository.deleteByUserAndType(userEntity,
        ConfirmationCodeType.EMAIL_CONFIRMATION.getValue());

    userEntity.setEmailConfirmed(Boolean.TRUE);
    userRepository.save(userEntity);

    return userMapper.toUserResponse(userEntity);
  }

  @Override
  @Transactional
  public UserLoginResponse login(final UserLoginRequest userLoginRequest) throws ApiException {
    final Authentication authenticationData = new UsernamePasswordAuthenticationToken(
        userLoginRequest.getUsername(), userLoginRequest.getPassword());

    final Authentication authenticationResult = authenticationManager.authenticate(
        authenticationData);
    final UserEntity authenticatedUser = (UserEntity) authenticationResult.getPrincipal();

    return createUserLoginResponse(authenticatedUser);
  }

  @Override
  @Transactional
  public UserLoginResponse refreshAccess(final AccessRefreshRequest accessRefreshRequest)
      throws ApiException {
    if (accessRefreshRequest.getRefreshToken() == null) {
      throw new UnauthenticatedException("Refresh token is not present in the cookies.");
    }

    final String refreshTokenOwner = tokenService.getRefreshTokenOwner(
        accessRefreshRequest.getRefreshToken());

    final UserEntity userEntity = userRepository.findByUsername(refreshTokenOwner).orElseThrow(
        () -> new NotFoundException("The owner of the refresh token could not be found."));

    Optional<RefreshTokenEntity> refreshTokenEntity = userEntity.getRefreshTokens().stream()
        .filter(refreshToken -> encoder.matches(accessRefreshRequest.getRefreshToken(),
            refreshToken.getValue())).findFirst();

    if (refreshTokenEntity.isEmpty() || refreshTokenEntity.get().getExpirationDate()
        .isBefore(LocalDateTime.now())) {
      throw new UnauthenticatedException("The refresh token is not valid.");
    }

    return createUserLoginResponse(userEntity);
  }

  @Override
  public GenericResponse sendPasswordResetCode(
      final PasswordResetCodeRequest passwordResetCodeRequest) throws ApiException {
    final UserEntity user = findUserByEmail(passwordResetCodeRequest.getUserEmail());

    if (Boolean.FALSE.equals(user.getEmailConfirmed())) {
      throw new UnauthorizedException("Email address has not been confirmed.");
    }

    Optional<ConfirmationCodeEntity> passwordResetCode = confirmationCodeRepository.findByUserAndType(
        user,
        ConfirmationCodeType.PASSWORD_RESET.getValue());

    ConfirmationCodeEntity passwordResetCodeEntity = new ConfirmationCodeEntity();
    if (passwordResetCode.isPresent()) {
      passwordResetCodeEntity = passwordResetCode.get();
    }

    final String passwordResetCodeValue = tokenService.generateOtp(passwordResetCodeLength);
    passwordResetCodeEntity.setValue(encoder.encode(passwordResetCodeValue));
    passwordResetCodeEntity.setType(ConfirmationCodeType.PASSWORD_RESET.getValue());
    passwordResetCodeEntity.setUser(user);
    passwordResetCodeEntity.setExpiresAt(
        LocalDateTime.now().plusMinutes(passwordResetCodeExpirationMinutes));
    confirmationCodeRepository.save(passwordResetCodeEntity);

    emailService.sendPlaintextEmail(EmailSubject.PASSWORD_RESET, user.getEmail(),
        passwordResetCodeValue);

    GenericResponse genericResponse = new GenericResponse();
    genericResponse.setMessage(String.format("Password reset code is successfully sent to %s.",
        user.getEmail()));

    return genericResponse;
  }

  @Override
  public GenericResponse resetPassword(final PasswordResetRequest passwordResetRequest)
      throws ApiException {
    final UserEntity userEntity = findUserByEmail(passwordResetRequest.getUserEmail());

    if (Boolean.FALSE.equals(userEntity.getEmailConfirmed())) {
      throw new UnauthorizedException("Email address has not been confirmed.");
    }

    final ConfirmationCodeEntity passwordResetCodeEntity = confirmationCodeRepository.findByUserAndType(
            userEntity, ConfirmationCodeType.PASSWORD_RESET.getValue())
        .orElseThrow(() -> new BadRequestException(
            String.format("None password reset code has been issued to %s.",
                userEntity.getEmail())));

    if (!encoder.matches(passwordResetRequest.getPasswordResetCode(),
        passwordResetCodeEntity.getValue())) {
      throw new BadRequestException("Provided reset code does not match the existing one.");
    }
    if (passwordResetCodeEntity.getExpiresAt().isBefore(LocalDateTime.now())) {
      throw new BadRequestException("Reset code has expired.");
    }

    userEntity.setPassword(encoder.encode(passwordResetRequest.getNewPassword()));

    userRepository.save(userEntity);
    confirmationCodeRepository.delete(passwordResetCodeEntity);

    GenericResponse genericResponse = new GenericResponse();
    genericResponse.setMessage("Password is successfully updated.");
    return genericResponse;
  }

  private UserLoginResponse createUserLoginResponse(final UserEntity userEntity) {
    final String accessToken = tokenService.generateAccessToken(userEntity);
    final String refreshToken = createRefreshToken(userEntity);

    final UserLoginResponse userLoginResponse = new UserLoginResponse();
    userLoginResponse.setAccessToken(accessToken);
    userLoginResponse.setUsername(userEntity.getUsername());
    userLoginResponse.setRefreshToken(refreshToken);
    userLoginResponse.setRefreshTokenExpirationSeconds((int)
        Duration.ofMinutes(refreshTokenExpirationMinutes).getSeconds());
    userLoginResponse.setRoles(
        userEntity.getRoles().stream().map(RoleEntity::getName).toList());

    return userLoginResponse;
  }

  private String createRefreshToken(final UserEntity userEntity) {
    final String refreshToken = tokenService.generateRefreshToken(userEntity);

    final RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
    refreshTokenEntity.setUser(userEntity);
    refreshTokenEntity.setValue(encoder.encode(refreshToken));
    refreshTokenEntity.setExpirationDate(
        LocalDateTime.now().plusMinutes(refreshTokenExpirationMinutes));

    refreshTokenRepository.save(refreshTokenEntity);

    return refreshToken;
  }

  private void validateConfirmationCode(final String confirmationCode,
      final ConfirmationCodeEntity confirmationCodeEntity) throws BadRequestException {
    if (LocalDateTime.now().isAfter(confirmationCodeEntity.getExpiresAt())) {
      throw new BadRequestException("Existing confirmation code has expired.");
    }

    if (!encoder.matches(confirmationCode, confirmationCodeEntity.getValue())) {
      throw new BadRequestException("Provided confirmation code is not correct.");
    }
  }

  private void sendEmailConfirmationCode(final UserEntity userEntity,
      final String confirmationCode) {
    emailService.sendPlaintextEmail(EmailSubject.EMAIL_CONFIRMATION,
        userEntity.getEmail(), confirmationCode);
  }

  private void deleteExistingEmailConfirmationCode(final UserEntity userEntity) {
    if (!confirmationCodeRepository.existsByUserAndType(userEntity,
        ConfirmationCodeType.EMAIL_CONFIRMATION.getValue())) {
      return;
    }

    confirmationCodeRepository.deleteByUserAndType(userEntity,
        ConfirmationCodeType.EMAIL_CONFIRMATION.getValue());
  }

  private UserEntity findUserByEmail(final String userEmail) throws NotFoundException {
    return userRepository.findByEmail(userEmail)
        .orElseThrow(() -> new NotFoundException("User with the provided email does not exist."));
  }

  private String createEmailConfirmationCode(final UserEntity userEntity) {
    final String confirmationCodeValue = tokenService.generateOtp(emailConfirmationCodeLength);

    final ConfirmationCodeEntity confirmationCodeEntity = new ConfirmationCodeEntity();
    confirmationCodeEntity.setType(ConfirmationCodeType.EMAIL_CONFIRMATION.getValue());
    confirmationCodeEntity.setValue(encoder.encode(confirmationCodeValue));
    confirmationCodeEntity.setUser(userEntity);
    confirmationCodeEntity.setExpiresAt(
        LocalDateTime.now().plusMinutes(emailConfirmationCodeExpirationMinutes));

    confirmationCodeRepository.save(confirmationCodeEntity);
    return confirmationCodeValue;
  }

  private <T> void validateUniqueFieldConstraint(final Predicate<T> existsByField,
      final T fieldValue, final String fieldName) throws BadRequestException {
    if (Boolean.TRUE.equals(existsByField.test(fieldValue))) {
      throw new BadRequestException(String.format("Provided %s is already taken.", fieldName));
    }
  }

  @Override
  public List<UserResponse> getAllUsers() {
    List<UserEntity> users = userRepository.findAll();
    List<UserResponse> userResponses = new ArrayList<>();

    for (UserEntity userEntity : users) {
      userResponses.add(userMapper.toUserResponse(userEntity));
    }

    return userResponses;
  }

}