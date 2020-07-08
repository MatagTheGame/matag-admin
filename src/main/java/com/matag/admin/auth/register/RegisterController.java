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
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class RegisterController {
  private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

  private static final String EMAIL_ALREADY_REGISTERED = "This email is already registered (use reset password functionality).";
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

  @PostMapping("/register")
  public RegisterResponse register(@RequestBody RegisterRequest request) {
    LOGGER.info("User " + request.email() + " registering with username[" + request.username() + "].");

    validate(request);

    registerService.register(request.email(), request.username(), request.password());
    LOGGER.info("Registration successful.");

    return new RegisterResponse(REGISTERED_VERIFY_EMAIL);
  }

  @GetMapping("/verify")
  public VerifyResponse verify(@Param("username") String username, @Param("code") String code) {
    LOGGER.info("Verifying " + username + " with code " + code);
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
    emailValidator.validate(request.email());
    usernameValidator.validate(request.username());
    passwordValidator.validate(request.password());

    if (userRepository.findByEmailAddress(request.email()).isPresent()) {
      throw new ValidationException(EMAIL_ALREADY_REGISTERED);
    }

    if (userRepository.findByUsername(request.username()).isPresent()) {
      throw new ValidationException(USERNAME_ALREADY_REGISTERED);
    }
  }
}
