package com.bookify.rest.advice;

import com.bookify.api.enums.ApiErrorType;
import com.bookify.api.model.error.ApiError;
import com.bookify.api.model.error.ApiErrorResponse;
import com.bookify.api.model.exception.ApiException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ErrorHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValid(
      final MethodArgumentNotValidException exception) {
    final BindingResult bindingResult = exception.getBindingResult();
    final List<FieldError> fieldErrors = bindingResult.getFieldErrors();

    return new ResponseEntity<>(createApiModelValidationErrorResponse(fieldErrors,
        DefaultMessageSourceResolvable::getDefaultMessage),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ApiErrorResponse> handleMissingRequestBody(
      final HttpMessageNotReadableException exception) {
    return new ResponseEntity<>(
        new ApiErrorResponse(
            List.of(new ApiError(ApiErrorType.BAD_REQUEST, "Request body is not properly set."))),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ResponseEntity<ApiErrorResponse> handeInvalidMediaType(
      final HttpMediaTypeNotSupportedException exception) {
    return new ResponseEntity<>(
        new ApiErrorResponse(
            List.of(
                new ApiError(ApiErrorType.BAD_REQUEST, "Defined media type is not supported."))),
        HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(ApiException.class)
  public ResponseEntity<ApiErrorResponse> handleApiException(final ApiException exception) {
    final List<ApiError> errors = new ArrayList<>();
    errors.add(new ApiError(ApiErrorType.BUSINESS_LOGIC, exception.getMessage()));

    return new ResponseEntity<>(new ApiErrorResponse(errors), exception.getStatusCode());
  }

  @ExceptionHandler(BadCredentialsException.class)
  public ResponseEntity<ApiErrorResponse> handleBadCredentialsException(
      final BadCredentialsException badCredentialsException) {
    final List<ApiError> errors = new ArrayList<>();
    errors.add(new ApiError(ApiErrorType.AUTHORIZATION, "Provided credentials are not correct."));

    return new ResponseEntity<>(new ApiErrorResponse(errors), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(DisabledException.class)
  public ResponseEntity<ApiErrorResponse> handleDisabledException(
      final DisabledException disabledException) {
    final List<ApiError> errors = new ArrayList<>();
    errors.add(new ApiError(ApiErrorType.AUTHORIZATION, "Account has not been confirmed."));

    return new ResponseEntity<>(new ApiErrorResponse(errors), HttpStatus.UNAUTHORIZED);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiErrorResponse> handleException(final Exception exception) {
    final List<ApiError> errors = new ArrayList<>();
    errors.add(new ApiError(ApiErrorType.INTERNAL_SERVER, "Something went wrong."));

    return new ResponseEntity<>(new ApiErrorResponse(errors), HttpStatus.INTERNAL_SERVER_ERROR);
  }

  private <T> ApiErrorResponse createApiModelValidationErrorResponse(Collection<T> errorCollection,
      Function<T, String> errorMessageGetter) {
    if (errorCollection == null || errorCollection.isEmpty()) {
      return new ApiErrorResponse(new ArrayList<>());
    }

    return new ApiErrorResponse(errorCollection.stream()
        .map(error -> new ApiError(ApiErrorType.MODEL_VALIDATION, errorMessageGetter.apply(error)))
        .toList());
  }

}