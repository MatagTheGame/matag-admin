package com.matag.admin.auth.register;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.matag.admin.auth.codes.RandomCodeService;
import com.matag.admin.user.MatagUser;
import com.matag.admin.user.verification.MatagUserVerification;
import com.matag.admin.user.verification.MatagUserVerificationRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UserVerifyService {
  private static final int MAX_ATTEMPTS = 3;

  private final RandomCodeService randomCodeService;
  private final MatagUserVerificationRepository userVerificationRepository;
  private final Clock clock;

  public MatagUserVerification createVerification(MatagUser user) {
    var verificationCode = randomCodeService.generatesRandomCode();
    var matagUserVerification = MatagUserVerification.builder()
      .verificationCode(verificationCode)
      .matagUser(user)
      .validUntil(LocalDateTime.now(clock).plusDays(1))
      .attempts(0)
      .build();

    userVerificationRepository.save(matagUserVerification);

    return matagUserVerification;
  }

  public void verify(MatagUser user, String code) {
    var matagUserVerification = user.getMatagUserVerification();
    if (matagUserVerification.getAttempts() >= MAX_ATTEMPTS) {
      increaseAttempts(matagUserVerification);
      throw new RuntimeException("User " + user.getUsername() + " too many attempts. times: " + matagUserVerification.getAttempts());
    }

    if (LocalDateTime.now(clock).isAfter(matagUserVerification.getValidUntil())) {
      increaseAttempts(matagUserVerification);
      throw new RuntimeException("User " + user.getUsername() + " validation code expired on: " + matagUserVerification.getValidUntil() + " . times: " + matagUserVerification.getAttempts());
    }

    if (!matagUserVerification.getVerificationCode().equals(code)) {
      increaseAttempts(matagUserVerification);
      throw new RuntimeException("User " + user.getUsername() + " attempting a wrong code: " + code + " times: " + matagUserVerification.getAttempts());
    }

    matagUserVerification.setVerificationCode(null);
    matagUserVerification.setValidUntil(null);
    matagUserVerification.setAttempts(0);
    userVerificationRepository.save(matagUserVerification);
  }

  @Transactional(propagation = Propagation.REQUIRES_NEW)
  public void increaseAttempts(MatagUserVerification matagUserVerification) {
    matagUserVerification.setAttempts(matagUserVerification.getAttempts() + 1);
    userVerificationRepository.save(matagUserVerification);
  }
}
