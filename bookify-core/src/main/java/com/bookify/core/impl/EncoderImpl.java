package com.bookify.core.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class EncoderImpl implements PasswordEncoder {

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public EncoderImpl() {
    this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
  }

  @Override
  public String encode(final CharSequence rawText) {
    return bCryptPasswordEncoder.encode(rawText);
  }

  @Override
  public boolean matches(final CharSequence rawText, final String encodedText) {
    return bCryptPasswordEncoder.matches(rawText, encodedText);
  }

}