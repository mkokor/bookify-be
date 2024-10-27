package com.bookify.core.impl;

import com.bookify.api.TokenService;
import com.bookify.core.util.JwtUtils;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class TokenServiceImpl implements TokenService {

  public static final String AUTHORITIES = "authorities";
  public static final String DIGITS = "0123456789";

  @Value("${access.token.expiration.minutes}")
  private int accessTokenExpirationMinutes;

  @Value("${access.token.secret}")
  private String accessTokenSecret;

  @Value("${refresh.token.expiration.minutes}")
  private int refreshTokenExpirationMinutes;

  @Value("${refresh.token.secret}")
  private String refreshTokenSecret;

  @Value("${jwt.issuer}")
  private String jwtIssuer;

  private final SecureRandom secureRandom = new SecureRandom();

  @Override
  public String generateAccessToken(final UserDetails userDetails) {
    final Map<String, Object> claims = new HashMap<>();

    claims.put(AUTHORITIES,
        userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

    return JwtUtils.generateJwt(claims, jwtIssuer, userDetails.getUsername(),
        accessTokenExpirationMinutes * (long) 60000, accessTokenSecret);
  }

  @Override
  public String getAccessTokenOwner(final String accessToken) {
    return JwtUtils.extractSubject(accessToken, accessTokenSecret);
  }

  @Override
  public boolean isAccessTokenValid(final String accessToken, final UserDetails userDetails) {
    final String username = JwtUtils.extractSubject(accessToken, accessTokenSecret);
    return username.equals(userDetails.getUsername()) && !JwtUtils.isJwtExpired(accessToken,
        accessTokenSecret);
  }

  @Override
  public String generateOtp(int length) {
    final StringBuilder otpBuilder = new StringBuilder();

    for (int i = 0; i < length; i++) {
      int randomIndex = secureRandom.nextInt(DIGITS.length());
      otpBuilder.append(DIGITS.charAt(randomIndex));
    }

    return otpBuilder.toString();
  }

  @Override
  public String generateRefreshToken(final UserDetails userDetails) {
    final Map<String, Object> claims = new HashMap<>();

    claims.put(AUTHORITIES,
        userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList());

    return JwtUtils.generateJwt(claims, jwtIssuer, userDetails.getUsername(),
        refreshTokenExpirationMinutes * (long) 60000, refreshTokenSecret);
  }

  @Override
  public String getRefreshTokenOwner(final String refreshToken) {
    return JwtUtils.extractSubject(refreshToken, refreshTokenSecret);
  }

}