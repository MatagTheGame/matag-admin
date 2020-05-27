package com.matag.admin.auth.register;

import com.matag.admin.auth.codes.RandomCodeService;
import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import com.matag.admin.user.verification.MatagUserVerification;
import com.matag.admin.user.verification.MatagUserVerificationRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import static com.matag.admin.user.MatagUserStatus.ACTIVE;
import static com.matag.admin.user.MatagUserStatus.VERIFYING;
import static com.matag.admin.user.MatagUserType.USER;

@Component
@AllArgsConstructor
public class RegisterService {
  private final PasswordEncoder passwordEncoder;
  private final Clock clock;
  private final MatagUserRepository userRepository;
  private final MatagUserVerificationRepository userVerificationRepository;
  private final RandomCodeService randomCodeService;
  private final RegisterEmailService registerEmailService;

  @Transactional
  public void register(String email, String username, String password) {
    String verificationCode = randomCodeService.generatesRandomCode();

    MatagUser user = MatagUser.builder()
      .username(username)
      .password(passwordEncoder.encode(password))
      .emailAddress(email)
      .createdAt(LocalDateTime.now(clock))
      .updatedAt(LocalDateTime.now(clock))
      .status(VERIFYING)
      .type(USER)
      .build();

    MatagUserVerification matagUserVerification = MatagUserVerification.builder()
      .verificationCode(verificationCode)
      .matagUser(user)
      .validUntil(LocalDateTime.now(clock).plusDays(1))
      .attempts(0)
      .build();

    userRepository.save(user);
    userVerificationRepository.save(matagUserVerification);

    registerEmailService.sendRegistrationEmail(email, username, verificationCode);
  }

  public void activate(String username, String code) {
    Optional<MatagUser> userOptional = userRepository.findByUsername(username);
    if (userOptional.isEmpty()) {
      throw new RuntimeException("User " + username + " not found.");
    }

    MatagUser user = userOptional.get();
    switch (user.getStatus()) {
      case INACTIVE:
        throw new RuntimeException("User " + username + " is inactive and cannot be activated.");

      case VERIFYING:
        MatagUserVerification matagUserVerification = user.getMatagUserVerification();
        if (!matagUserVerification.getVerificationCode().equals(code)) {
          matagUserVerification.setAttempts(matagUserVerification.getAttempts() + 1);
          throw new RuntimeException("User " + username + " attempting a wrong code: " + code + " times: " + matagUserVerification.getAttempts());
        }

        user.setStatus(ACTIVE);
        user.setUpdatedAt(LocalDateTime.now(clock));
        userRepository.save(user);

        matagUserVerification.setVerificationCode(null);
        matagUserVerification.setValidUntil(null);
        matagUserVerification.setAttempts(0);
        userVerificationRepository.save(matagUserVerification);
    }
  }
}
