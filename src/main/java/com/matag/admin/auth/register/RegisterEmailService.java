package com.matag.admin.auth.register;

import com.matag.admin.config.ConfigService;
import javax.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class RegisterEmailService {
  private final JavaMailSender emailSender;
  private final ConfigService configService;

  @SneakyThrows
  public void sendRegistrationEmail(String email, String username, String verificationCode) {
    MimeMessage mimeMessage = emailSender.createMimeMessage();
    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
    helper.setTo(email);
    helper.setSubject("Matag: The Game - Registration");
    helper.setText(createBody(username, verificationCode), true);
    emailSender.send(mimeMessage);
  }

  private String createBody(String username, String verificationCode) {
    return
        "<p>Hi " + username + ",</p>" +
        "<p>Welcome to <a href=\"" + configService.getMatagAdminUrl() + "\">Matag: The Game</a>.</p>" +
        "<p>Please <a href=\"" + configService.getMatagAdminUrl() + "/ui/admin/auth/verify?code=" + verificationCode + "\">click here</a> to verify your account.</p>" +
        "<p>The Matag: The Game Team.</p>";
  }
}
