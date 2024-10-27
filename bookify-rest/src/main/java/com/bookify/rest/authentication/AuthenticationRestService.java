package com.bookify.rest.authentication;

import com.bookify.api.AuthenticationService;
import com.bookify.api.model.GenericResponse;
import com.bookify.api.model.authentication.AccessRefreshRequest;
import com.bookify.api.model.authentication.PasswordResetCodeRequest;
import com.bookify.api.model.authentication.PasswordResetRequest;
import com.bookify.api.model.authentication.UserLoginRequest;
import com.bookify.api.model.authentication.UserLoginResponse;
import com.bookify.api.model.confirmationcode.EmailConfirmationCodeUpdateRequest;
import com.bookify.api.model.confirmationcode.EmailConfirmationRequest;
import com.bookify.api.model.exception.ApiException;
import com.bookify.api.model.user.UserRegistrationRequest;
import com.bookify.api.model.user.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "authentication", description = "Authentication API")
@RestController
@RequestMapping(value = "/auth", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AuthenticationRestService {

  private final AuthenticationService authenticationService;
  public static final String REFRESH_TOKEN_COOKIE = "refresh_token";

  @Operation(summary = "Registration")
  @PostMapping(value = "/registration", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> register(
      @Valid @RequestBody final UserRegistrationRequest userRegistrationRequest)
      throws ApiException {
    return new ResponseEntity<>(authenticationService.register(userRegistrationRequest),
        HttpStatus.CREATED);
  }

  @Operation(summary = "Resend email confirmation code")
  @PostMapping(value = "/email-confirmation-code", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GenericResponse> resendEmailConfirmationCode(
      @Valid @RequestBody final EmailConfirmationCodeUpdateRequest emailConfirmationCodeUpdateRequest)
      throws ApiException {
    return new ResponseEntity<>(
        authenticationService.resendEmailConfirmationCode(emailConfirmationCodeUpdateRequest),
        HttpStatus.OK);
  }

  @Operation(summary = "Confirm email address")
  @PostMapping(value = "/email-confirmation", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserResponse> confirmEmailAddress(
      @Valid @RequestBody final EmailConfirmationRequest emailConfirmationRequest)
      throws ApiException {
    return new ResponseEntity<>(authenticationService.confirmEmailAddress(emailConfirmationRequest),
        HttpStatus.OK);
  }

  @Operation(summary = "Login")
  @PostMapping(value = "/login", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserLoginResponse> login(
      @Valid @RequestBody final UserLoginRequest userLoginRequest,
      final HttpServletResponse httpServletResponse)
      throws ApiException {
    final UserLoginResponse userLoginResponse = authenticationService.login(userLoginRequest);

    addRefreshTokenCookie(httpServletResponse, userLoginResponse);
    return new ResponseEntity<>(userLoginResponse, HttpStatus.OK);
  }

  @Operation(summary = "Refresh access")
  @PostMapping(value = "/access-refresh", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserLoginResponse> refreshAccess(
      @CookieValue(name = REFRESH_TOKEN_COOKIE) final String refreshToken,
      final HttpServletResponse httpServletResponse)
      throws ApiException {
    final AccessRefreshRequest accessRefreshRequest = new AccessRefreshRequest();
    accessRefreshRequest.setRefreshToken(refreshToken);

    final UserLoginResponse userLoginResponse = authenticationService.refreshAccess(
        accessRefreshRequest);

    addRefreshTokenCookie(httpServletResponse, userLoginResponse);
    return new ResponseEntity<>(authenticationService.refreshAccess(accessRefreshRequest),
        HttpStatus.OK);
  }

  @Operation(summary = "Send/resend password reset code")
  @PostMapping(value = "/{email}/password-reset-code", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GenericResponse> sendPasswordResetCode(@PathVariable final String email)
      throws ApiException {
    PasswordResetCodeRequest passwordResetCodeRequest = new PasswordResetCodeRequest();
    passwordResetCodeRequest.setUserEmail(email);

    return new ResponseEntity<>(
        authenticationService.sendPasswordResetCode(passwordResetCodeRequest), HttpStatus.OK);
  }

  @Operation(summary = "Reset password")
  @PostMapping(value = "/{email}/password-reset", consumes = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<GenericResponse> resetPassword(@PathVariable final String email,
      @RequestBody final PasswordResetRequest passwordResetRequest) throws ApiException {
    passwordResetRequest.setUserEmail(email);

    return new ResponseEntity<>(authenticationService.resetPassword(passwordResetRequest),
        HttpStatus.OK);
  }

  private void addRefreshTokenCookie(final HttpServletResponse httpServletResponse,
      UserLoginResponse userLoginResponse) {
    httpServletResponse.addCookie(
        createCookie(REFRESH_TOKEN_COOKIE, userLoginResponse.getRefreshToken(), Boolean.TRUE,
            userLoginResponse.getRefreshTokenExpirationSeconds()));
  }

  private Cookie createCookie(final String name, final String value, final boolean httpOnly,
      final int maxAge) {
    final Cookie cookie = new Cookie(name, value);
    cookie.setHttpOnly(httpOnly);
    cookie.setMaxAge(maxAge);

    return cookie;
  }

}