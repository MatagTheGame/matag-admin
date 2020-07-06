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
    var mimeMessage = emailSender.createMimeMessage();
    var helper = new MimeMessageHelper(mimeMessage, "utf-8");
    helper.setTo(email);
    helper.setSubject(configService.getMatagName() + " - Registration");
    helper.setText(createBody(username, verificationCode), true);
    emailSender.send(mimeMessage);
  }

  private String createBody(String username, String verificationCode) {
    var verificationLink = configService.getMatagAdminUrl() + "/ui/admin/auth/verify?" +
      "username=" + username + "&" +
      "code=" + verificationCode;

    return
      "<p>Hi " + username + ",</p>" +
        "<p>Welcome to <a href=\"" + configService.getMatagAdminUrl() + "\">" + configService.getMatagName() + "</a>.</p>" +
        "<p>Please <a href=\"" + verificationLink + "\">click here</a> to verify your account.</p>" +
        "<p>The <em>" + configService.getMatagName() + "</em> Team.</p>";
  }
}
