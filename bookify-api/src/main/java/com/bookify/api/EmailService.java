package com.bookify.api;

import com.bookify.api.enums.EmailSubject;

public interface EmailService {

  void sendPlaintextEmail(final EmailSubject subject, final String recipientEmailAddress,
      final String text);

}