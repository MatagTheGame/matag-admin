package com.matag.admin.auth.register;

import static com.matag.admin.user.MatagUserStatus.VERIFYING;
import static com.matag.admin.user.MatagUserType.USER;

import com.matag.admin.auth.codes.RandomCodeService;
import com.matag.admin.user.MatagUser;
import com.matag.admin.user.MatagUserRepository;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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

    MatagUser matagUser = new MatagUser();
    matagUser.setUsername(username);
    matagUser.setPassword(passwordEncoder.encode(password));
    matagUser.setEmailAddress(email);
    matagUser.setCreatedAt(LocalDateTime.now(clock));
    matagUser.setUpdatedAt(LocalDateTime.now(clock));
    matagUser.setStatus(VERIFYING);
    matagUser.setType(USER);
    matagUser.setVerificationCode(verificationCode);
    userRepository.save(matagUser);

    registerEmailService.sendRegistrationEmail(email, username, verificationCode);
  }
}
