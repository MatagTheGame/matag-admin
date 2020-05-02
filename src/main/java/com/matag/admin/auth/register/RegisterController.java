package com.matag.admin.auth.register;

import com.matag.admin.auth.validators.EmailValidator;
import com.matag.admin.auth.validators.UsernameValidator;
import com.matag.admin.config.ConfigService;
import com.matag.admin.user.MatagUserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class RegisterController {
  private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

  private static final String EMAIL_IS_INVALID = "Email is invalid.";
  private static final String USERNAME_INVALID = "Username needs to be between 4 and 25 characters and can contains only letters  number and one of the following characters: [+ - * = _ . @ &].";
  private static final String PASSWORD_IS_INVALID = "Password is invalid.";
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

  @PostMapping("/register")
  public ResponseEntity<RegisterResponse> register(@RequestBody RegisterRequest request) {
    LOGGER.info("User " + request.getEmail() + " registering with username[" + request.getUsername() + "].");

    if (!emailValidator.isValid(request.getEmail())) {
      return error(EMAIL_IS_INVALID);
    }

    if (!usernameValidator.isValid(request.getUsername())) {
      return error(USERNAME_INVALID);
    }

    if (request.getPassword().length() < 4) {
      return error(PASSWORD_IS_INVALID);
    }

    if (userRepository.findByEmailAddress(request.getEmail()).isPresent()) {
      return error(EMAIL_ALREADY_REGISTERED);
    }

    if (userRepository.findByUsername(request.getUsername()).isPresent()) {
      return error(USERNAME_ALREADY_REGISTERED);
    }

    registerService.register(request.getEmail(), request.getUsername(), request.getPassword());

    return ok(REGISTERED_VERIFY_EMAIL);
  }

  @GetMapping("/verify")
  public ResponseEntity<VerifyResponse> verify(@Param("username") String username, @Param("code") String code) {
    LOGGER.info("Verifying " + username + " with code " + code);
    try {
      registerService.activate(username, code);
      return ResponseEntity
        .status(OK)
        .body(VerifyResponse.builder()
          .message(ACCOUNT_VERIFICATION_CORRECT)
          .build()
        );

    } catch (Exception e) {
      LOGGER.warn(e.getMessage());
      return ResponseEntity
        .status(BAD_REQUEST)
        .body(VerifyResponse.builder()
          .error(ACCOUNT_VERIFICATION_ERROR.replace("SUPPORT_MAIL", configService.getMatagSupportEmail()))
          .build()
        );
    }
  }

  private ResponseEntity<RegisterResponse> ok(String message) {
    LOGGER.error("Registration successful.");
    return response(OK, message, null);
  }

  private ResponseEntity<RegisterResponse> error(String error) {
    LOGGER.error("Registration failed error=" + error);
    return response(BAD_REQUEST, null, error);
  }

  private ResponseEntity<RegisterResponse> response(HttpStatus status, String message, String error) {
    return ResponseEntity
      .status(status)
      .body(RegisterResponse.builder()
        .message(message)
        .error(error)
        .build());
  }
}
