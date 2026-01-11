package com.matag.admin.auth.register;

import com.matag.admin.auth.validators.EmailValidator;
import com.matag.admin.auth.validators.PasswordValidator;
import com.matag.admin.auth.validators.UsernameValidator;
import com.matag.admin.auth.validators.ValidationException;
import com.matag.admin.config.ConfigService;
import com.matag.admin.exception.MatagException;
import com.matag.admin.user.MatagUserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class RegisterController {
  private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

  private static final String EMAIL_ALREADY_REGISTERED = "This email is already registered.";
  private static final String USERNAME_ALREADY_REGISTERED = "This username is already registered (please choose a new one).";
  private static final String REGISTERED_VERIFY_EMAIL = "Registration Successful. Please check your email for a verification code.";
  private static final String ACCOUNT_VERIFICATION_CORRECT = "Your account has been correctly verified. Now you can proceed with logging in.";
  private static final String ACCOUNT_VERIFICATION_ERROR = "Your account could not be verified. Please send a message to SUPPORT_MAIL.";

  private final ConfigService configService;
  private final MatagUserRepository userRepository;
  private final EmailValidator emailValidator;
  private final UsernameValidator usernameValidator;
  private final RegisterService registerService;
  private final PasswordValidator passwordValidator;

  @PreAuthorize("permitAll()")
  @PostMapping("/register")
  public RegisterResponse register(@RequestBody RegisterRequest request) {
      LOGGER.info("User {} registering with username[{}].", request.getEmail(), request.getUsername());

    validate(request);

    registerService.register(request.getEmail(), request.getUsername(), request.getPassword());
    LOGGER.info("Registration successful.");

    return RegisterResponse.builder()
        .message(REGISTERED_VERIFY_EMAIL)
        .build();
  }

  @GetMapping("/verify")
  public VerifyResponse verify(@RequestParam("username") String username, @RequestParam("code") String code) {
      LOGGER.info("Verifying {} with code {}", username, code);
    try {
      registerService.activate(username, code);
      return VerifyResponse.builder()
          .message(ACCOUNT_VERIFICATION_CORRECT)
          .build();

    } catch (Exception e) {
      LOGGER.warn(e.getMessage());
      throw new MatagException(ACCOUNT_VERIFICATION_ERROR.replace("SUPPORT_MAIL", configService.getMatagSupportEmail()));
    }
  }

  private void validate(@RequestBody RegisterRequest request) {
    emailValidator.validate(request.getEmail());
    usernameValidator.validate(request.getUsername());
    passwordValidator.validate(request.getPassword());

    if (userRepository.findByEmailAddress(request.getEmail()).isPresent()) {
      throw new ValidationException(EMAIL_ALREADY_REGISTERED);
    }

    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
      throw new ValidationException(USERNAME_ALREADY_REGISTERED);
    }
  }
}
