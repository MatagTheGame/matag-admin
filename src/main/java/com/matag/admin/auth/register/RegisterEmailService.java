package com.matag.admin.auth.register;

import org.springframework.stereotype.Component;

import com.matag.admin.config.ConfigService;
import com.matag.admin.email.MatagEmailSender;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;

@Component
@AllArgsConstructor
public class RegisterEmailService {
  private final MatagEmailSender emailSender;
  private final ConfigService configService;

  @SneakyThrows
  public void sendRegistrationEmail(String receiver, String username, String verificationCode) {
    emailSender.send(receiver, "Registration", createBody(username, verificationCode));
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
