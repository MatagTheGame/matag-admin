package com.matag.admin.auth.register;

import static com.matag.admin.user.MatagUserStatus.ACTIVE;
import static com.matag.admin.user.MatagUserStatus.VERIFYING;
import static com.matag.admin.user.MatagUserType.USER;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.matag.admin.game.score.ScoreService;
import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import com.matag.admin.user.verification.MatagUserVerification;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class RegisterService {
  private final PasswordEncoder passwordEncoder;
  private final Clock clock;
  private final MatagUserRepository userRepository;
  private final ScoreService scoreService;
  private final RegisterEmailService registerEmailService;
  private final UserVerifyService userVerifyService;

  @Transactional
  public void register(String email, String username, String password) {
    var user = MatagUser.builder()
      .username(username)
      .password(passwordEncoder.encode(password))
      .emailAddress(email)
      .createdAt(LocalDateTime.now(clock))
      .updatedAt(LocalDateTime.now(clock))
      .status(VERIFYING)
      .type(USER)
      .build();
    userRepository.save(user);
    scoreService.createStartingScore(user);

    MatagUserVerification verification = userVerifyService.createVerification(user);

    registerEmailService.sendRegistrationEmail(email, username, verification.getVerificationCode());
  }

  public void activate(String username, String code) {
    var userOptional = userRepository.findByUsername(username);
    if (userOptional.isEmpty()) {
      throw new RuntimeException("User " + username + " not found.");
    }

    var user = userOptional.get();
    switch (user.getStatus()) {
      case INACTIVE:
        throw new RuntimeException("User " + username + " is inactive and cannot be activated.");

      case VERIFYING:
        userVerifyService.verify(user, code);

        user.setStatus(ACTIVE);
        user.setUpdatedAt(LocalDateTime.now(clock));
        userRepository.save(user);
    }
  }
}
