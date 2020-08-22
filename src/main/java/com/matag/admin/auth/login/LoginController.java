package com.matag.admin.auth.login;

import com.matag.admin.auth.validators.EmailValidator;
import com.matag.admin.auth.validators.PasswordValidator;
import com.matag.admin.auth.validators.ValidationException;
import com.matag.admin.session.AuthSessionFilter;
import com.matag.admin.session.MatagSession;
import com.matag.admin.session.MatagSessionRepository;
import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import com.matag.admin.user.MatagUserStatus;
import com.matag.admin.user.profile.CurrentUserProfileService;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static com.matag.admin.user.MatagUserType.GUEST;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class LoginController {
  private static final Logger LOGGER = LoggerFactory.getLogger(LoginController.class);

  public static final String EMAIL_USERNAME_OR_PASSWORD_ARE_INCORRECT = "Email/Username or password are not correct.";
  public static final String ACCOUNT_IS_NOT_ACTIVE = "Account is not active.";

  private final MatagUserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final MatagSessionRepository matagSessionRepository;
  private final EmailValidator emailValidator;
  private final PasswordValidator passwordValidator;
  private final CurrentUserProfileService currentUserProfileService;
  private final Clock clock;

  @PostMapping("/login")
  public LoginResponse login(@RequestBody LoginRequest loginRequest) {
    LOGGER.info("User " + loginRequest.getEmailOrUsername() + " logging in.");

    var user = validateLogin(loginRequest);

    if (user.getType() != GUEST) {
      var existingSession = matagSessionRepository.findByMatagUserId(user.getId());
      if (existingSession.isPresent()) {
        LOGGER.info("User was already logged in... restored its session.");
        existingSession.get().setValidUntil(LocalDateTime.now(clock).plusSeconds(AuthSessionFilter.SESSION_DURATION_TIME));
        matagSessionRepository.save(existingSession.get());
        return buildResponse(user, existingSession.get());
      }
    }

    MatagSession session = MatagSession.builder()
      .sessionId(UUID.randomUUID().toString())
      .matagUser(user)
      .createdAt(LocalDateTime.now(clock))
      .validUntil(LocalDateTime.now(clock).plusSeconds(AuthSessionFilter.SESSION_DURATION_TIME))
      .build();
    matagSessionRepository.save(session);

    LOGGER.info("Login successful.");
    return buildResponse(user, session);
  }

  private MatagUser validateLogin(@RequestBody LoginRequest loginRequest) {
    passwordValidator.validate(loginRequest.getPassword());

    var email = isEmailLogin(loginRequest);
    Optional<MatagUser> userOptional = getUsername(loginRequest.getEmailOrUsername(), email);

    if (userOptional.isEmpty()) {
      throw new InsufficientAuthenticationException(EMAIL_USERNAME_OR_PASSWORD_ARE_INCORRECT);
    }

    var user = userOptional.get();
    if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
      throw new InsufficientAuthenticationException(EMAIL_USERNAME_OR_PASSWORD_ARE_INCORRECT);
    }

    if (user.getStatus() != MatagUserStatus.ACTIVE) {
      throw new InsufficientAuthenticationException(ACCOUNT_IS_NOT_ACTIVE);
    }
    return user;
  }

  private boolean isEmailLogin(@RequestBody LoginRequest loginRequest) {
    try {
      emailValidator.validate(loginRequest.getEmailOrUsername());
      return true;
    } catch (ValidationException e) {
      return false;
    }
  }

  private Optional<MatagUser> getUsername(String emailOrUsername, boolean email) {
    if (email) {
      return userRepository.findByEmailAddress(emailOrUsername);
    } else {
      return userRepository.findByUsername(emailOrUsername);
    }
  }

  private LoginResponse buildResponse(MatagUser user, MatagSession session) {
    return LoginResponse.builder()
      .token(session.getSessionId())
      .profile(currentUserProfileService.getProfile(user, session))
      .build();
  }
}
