package com.matag.admin.auth.register;

import com.matag.admin.auth.codes.RandomCodeService;
import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
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
  private final RandomCodeService randomCodeService;
  private final RegisterEmailService registerEmailService;

  @Transactional
  public void register(String email, String username, String password) {
    String verificationCode = randomCodeService.generatesRandomCode();

    MatagUser user = new MatagUser();
    user.setUsername(username);
    user.setPassword(passwordEncoder.encode(password));
    user.setEmailAddress(email);
    user.setCreatedAt(LocalDateTime.now(clock));
    user.setUpdatedAt(LocalDateTime.now(clock));
    user.setStatus(VERIFYING);
    user.setType(USER);
    user.setVerificationCode(verificationCode);
    userRepository.save(user);

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
        if (!user.getVerificationCode().equals(code)) {
          throw new RuntimeException("User " + username + " attempting a wrong code: " + code);
        }

        user.setStatus(ACTIVE);
        user.setUpdatedAt(LocalDateTime.now(clock));
        userRepository.save(user);
    }
  }
}
