package com.matag.admin.email;

import com.matag.admin.config.ConfigService;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class MatagEmailSender {
  private final JavaMailSender emailSender;
  private final ConfigService configService;

  @SneakyThrows
  public void send(String receiver, String body) {
    var mimeMessage = emailSender.createMimeMessage();
    var helper = new MimeMessageHelper(mimeMessage, "utf-8");
    helper.setTo(receiver);
    helper.setSubject(configService.getMatagName() + " - Registration");
    helper.setText(body, true);
    emailSender.send(mimeMessage);
  }
}
