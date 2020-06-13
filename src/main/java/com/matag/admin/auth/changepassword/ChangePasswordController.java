package com.matag.admin.auth.changepassword;

import com.matag.admin.auth.register.RegisterRequest;
import com.matag.admin.auth.register.RegisterResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class ChangePasswordController {
  @PostMapping("/change-password")
  public ResponseEntity<RegisterResponse> changePassowrd(@RequestBody RegisterRequest request) {
    return null;
  }
}
