package com.matag.admin.game.score;

import static com.matag.admin.user.MatagUserType.USER;

import org.springframework.stereotype.Component;

import com.matag.admin.game.game.GameResultType;
import com.matag.admin.user.MatagUser;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EloApplyService {
  private final ScoreRepository scoreRepository;
  private final EloCalculationService eloCalculationService;

  public void apply(MatagUser user1, MatagUser user2, GameResultType result) {
    if (user1.getType() == USER && user2.getType() == USER) {
      var score1 = scoreRepository.findByMatagUser(user1);
      var score2 = scoreRepository.findByMatagUser(user2);
      eloCalculationService.applyEloRating(score1, score2, result);
    }
  }
}
