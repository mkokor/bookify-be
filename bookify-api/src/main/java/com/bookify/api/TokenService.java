package com.bookify.api;

import org.springframework.security.core.userdetails.UserDetails;

public interface TokenService {

  String generateAccessToken(final UserDetails userDetails);

  String getAccessTokenOwner(final String accessToken);

  boolean isAccessTokenValid(final String accessToken, final UserDetails userDetails);

  String generateOtp(final int length);

  String generateRefreshToken(final UserDetails userDetails);

  String getRefreshTokenOwner(final String refreshToken);

}