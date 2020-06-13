package com.matag.admin.auth.changepassword;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.auth.login.LoginController;
import com.matag.admin.auth.validators.PasswordValidator;
import com.matag.admin.auth.validators.ValidationException;
import com.matag.admin.exception.MatagException;
import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.LocalDateTime;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class ChangePasswordController {
  private final SecurityContextHolderHelper securityContextHolderHelper;
  private final PasswordEncoder passwordEncoder;
  private final PasswordValidator passwordValidator;
  private final Clock clock;
  private final MatagUserRepository userRepository;

  private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

  @PostMapping("/change-password")
  public ChangePasswordResponse changePassword(@RequestBody ChangePasswordRequest request) {
    MatagUser user = securityContextHolderHelper.getUser();
    if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
      throw new MatagException("Your password wasn't matched.");
    }

    try {
      passwordValidator.validate(request.getNewPassword());
    } catch (ValidationException e) {
      throw new ValidationException("The new password you chose is invalid: " + e.getMessage());
    }

    String newPasswordEncoded = passwordEncoder.encode(request.getNewPassword());
    user.setPassword(newPasswordEncoded);
    user.setUpdatedAt(LocalDateTime.now(clock));
    userRepository.save(user);
    LOGGER.info("Password successfully changed for user " + user.getUsername());

    return ChangePasswordResponse.builder().message("Password changed.").build();
  }
}
