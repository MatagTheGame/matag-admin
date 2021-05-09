package com.matag.admin.game.score;

import static com.matag.admin.game.game.GameType.UNLIMITED;

import java.time.Clock;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.matag.admin.user.MatagUser;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class ScoreService {
  private static final int STARTING_ELO = 1000;

  private final ScoreRepository scoreRepository;
  private final Clock clock;

  public void createStartingScore(MatagUser matagUser) {
    scoreRepository.save(Score.builder()
      .matagUser(matagUser)
      .elo(STARTING_ELO)
      .type(UNLIMITED)
      .wins(0)
      .draws(0)
      .losses(0)
      .lastGamePlayedAt(LocalDateTime.now(clock))
      .build());
  }
}
