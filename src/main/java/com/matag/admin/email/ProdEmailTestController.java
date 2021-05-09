package com.matag.admin.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class ProdEmailTestController {
  private static final Logger LOGGER = LoggerFactory.getLogger(ProdEmailTestController.class);
  private final MatagEmailSender matagEmailSender;

  @GetMapping("/email")
  public String testEmail() {
    try {
      matagEmailSender.send("antonioalonzi85@gmail.com", "Test", "email works.");
      return "Email Sent.";

    } catch (Exception e) {
      LOGGER.error("Error while sending email", e);
      return "Error while sending email: " + e.getMessage();
    }
  }
}
