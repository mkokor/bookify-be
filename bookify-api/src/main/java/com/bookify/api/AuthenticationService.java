package com.bookify.api;

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

import java.util.List;

public interface AuthenticationService {

  UserResponse register(final UserRegistrationRequest userRegistrationRequest) throws ApiException;

  GenericResponse resendEmailConfirmationCode(
      final EmailConfirmationCodeUpdateRequest emailConfirmationCodeUpdateRequest)
      throws ApiException;

  UserResponse confirmEmailAddress(final EmailConfirmationRequest emailConfirmationRequest)
      throws ApiException;

  UserLoginResponse login(final UserLoginRequest userLoginRequest) throws ApiException;

  UserLoginResponse refreshAccess(final AccessRefreshRequest accessRefreshRequest)
      throws ApiException;

  GenericResponse sendPasswordResetCode(final PasswordResetCodeRequest passwordResetCodeRequest)
      throws ApiException;

  GenericResponse resetPassword(final PasswordResetRequest passwordResetRequest)
      throws ApiException;

  List<UserResponse> getAllUsers();

}