package com.bookify.service.filter;

import com.bookify.api.TokenService;
import com.bookify.api.enums.ApiErrorType;
import com.bookify.api.model.error.ApiError;
import com.bookify.api.model.error.ApiErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class AccessTokenAuthenticationFilter extends OncePerRequestFilter {

  private final UserDetailsService userDetailsService;
  private final TokenService tokenService;

  public static final String ACCESS_TOKEN_IS_NOT_VALID = "Access token is not valid.";

  private String setAuthenticationErrorResponse(final String message) {
    try {
      final ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
          List.of(new ApiError(ApiErrorType.AUTHORIZATION, message)));
      return new ObjectMapper().writeValueAsString(apiErrorResponse);
    } catch (Exception exception) {
      return null;
    }
  }

  private void sendErrorResponse(final HttpServletResponse response, final String message)
      throws IOException {
    final String apiErrorResponse = setAuthenticationErrorResponse(message);

    if (apiErrorResponse == null) {
      response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
      response.getWriter().write("{ \"message\": \"Something went wrong.\" }");
      return;
    }

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.getWriter().write(apiErrorResponse);
  }

  @Override
  protected void doFilterInternal(@NonNull final HttpServletRequest request,
      @NonNull final HttpServletResponse response,
      @NonNull final FilterChain chain)
      throws ServletException, IOException {
    if (request.getServletPath().contains("/auth/")
        || SecurityContextHolder.getContext().getAuthentication() != null) {
      chain.doFilter(request, response);
      return;
    }

    final String authorizationHeader = request.getHeader("Authorization");
    if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
      sendErrorResponse(response, "Access token is not provided in valid format.");
      return;
    }

    String accessToken = authorizationHeader.substring(7);
    String username;
    try {
      username = tokenService.getAccessTokenOwner(accessToken);
    } catch (Exception exception) {
      sendErrorResponse(response, ACCESS_TOKEN_IS_NOT_VALID);
      return;
    }

    UserDetails userDetails;
    try {
      userDetails = this.userDetailsService.loadUserByUsername(username);
    } catch (final UsernameNotFoundException exception) {
      sendErrorResponse(response, "Access token owner could not be resolved.");
      return;
    }

    if (!tokenService.isAccessTokenValid(accessToken, userDetails)) {
      sendErrorResponse(response, ACCESS_TOKEN_IS_NOT_VALID);
      return;
    }

    UsernamePasswordAuthenticationToken principalDetails = new UsernamePasswordAuthenticationToken(
        userDetails, null,
        userDetails.getAuthorities());
    principalDetails.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
    SecurityContextHolder.getContext().setAuthentication(principalDetails);

    chain.doFilter(request, response);
  }

}