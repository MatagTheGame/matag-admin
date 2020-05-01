package com.matag.admin.auth.register;

import com.matag.admin.auth.validators.EmailValidator;
import com.matag.admin.auth.validators.UsernameValidator;
import com.matag.admin.user.MatagUserRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class RegisterController {
  private static final Logger LOGGER = LoggerFactory.getLogger(RegisterController.class);

  public static final String EMAIL_IS_INVALID = "Email is invalid.";
  public static final String USERNAME_INVALID = "Username needs to be between 4 and 25 characters and can contains only letters  number and one of the following characters: [+ - * = _ . @ &].";
  public static final String PASSWORD_IS_INVALID = "Password is invalid.";
  public static final String EMAIL_ALREADY_REGISTERED = "This email is already registered (use reset password functionality).";
  public static final String USERNAME_ALREADY_REGISTERED = "This username is already registered (please choose a new one).";
  public static final String REGISTERED_VERIFY_EMAIL = "Registration Successful. Please check your email for a verification code.";

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
