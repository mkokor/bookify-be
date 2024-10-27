package com.bookify.core.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

public final class JwtUtils {

  public static final String JTI_CLAIM = "jti";

  private JwtUtils() {
  }

  public static String generateJwt(final Map<String, Object> claims, final String issuer,
      final String subject, final long expirationMilliseconds,
      final String signKeySecret) {
    long currentMilliseconds = System.currentTimeMillis();

    claims.put(JTI_CLAIM, UUID.randomUUID().toString());

    final JwtBuilder jwtBuilder = Jwts.builder()
        .setIssuer(issuer)
        .setIssuedAt(new Date(currentMilliseconds))
        .setSubject(subject)
        .setExpiration(new Date(currentMilliseconds + expirationMilliseconds))
        .addClaims(claims)
        .signWith(generateSignKey(signKeySecret), SignatureAlgorithm.HS256);

    return jwtBuilder.compact();
  }


  public static String extractSubject(final String jwt, final String signKeySecret) {
    return extractClaim(jwt, signKeySecret, Claims::getSubject);
  }

  public static Date extractExpiration(final String jwt, final String signKeySecret) {
    return extractClaim(jwt, signKeySecret, Claims::getExpiration);
  }

  public static <T> T extractClaim(final String jwt, final String signKeySecret,
      final Function<Claims, T> claimsResolver) {
    return claimsResolver.apply(extractAllClaims(jwt, signKeySecret));
  }

  public static boolean isJwtExpired(final String jwt, final String signKeySecret) {
    return new Date().after(extractExpiration(jwt, signKeySecret));
  }

  public static Claims extractAllClaims(final String jwt, final String signKeySecret) {
    return Jwts.parserBuilder().setSigningKey(generateSignKey(signKeySecret)).build()
        .parseClaimsJws(jwt).getBody();
  }

  private static Key generateSignKey(final String signKeySecret) {
    byte[] keyBytes = Decoders.BASE64.decode(signKeySecret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

}