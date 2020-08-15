package com.matag.admin.email;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@AllArgsConstructor
public class ProdEmailTestController {
  private final MatagEmailSender matagEmailSender;

  @GetMapping("/email")
  public void testEmail() {
    matagEmailSender.send("antonioalonzi85@gmail.com", "matag email sender works.");
  }
}
