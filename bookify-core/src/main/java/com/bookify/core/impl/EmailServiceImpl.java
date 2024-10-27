package com.bookify.core.impl;

import com.bookify.api.EmailService;
import com.bookify.api.enums.EmailSubject;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender javaMailSender;

  @Value("${email.sender.name}")
  private String emailSenderName;

  @Override
  public void sendPlaintextEmail(final EmailSubject subject, final String recipientEmailAddress,
      final String text) {
    final SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
    simpleMailMessage.setFrom(emailSenderName);
    simpleMailMessage.setSentDate(new Date());
    simpleMailMessage.setTo(recipientEmailAddress);
    simpleMailMessage.setSubject(subject.getValue());
    simpleMailMessage.setText(text);

    javaMailSender.send(simpleMailMessage);
  }

}