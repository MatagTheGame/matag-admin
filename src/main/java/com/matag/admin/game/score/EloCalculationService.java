package com.matag.admin.game.score;

import org.springframework.stereotype.Component;

import com.matag.admin.game.game.GameResultType;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class EloCalculationService {
  private static final int K = 100;

  public void applyEloRating(Score score1, Score score2, GameResultType result) {
    float p1 = probability(score1.getElo(), score2.getElo());
    float p2 = probability(score2.getElo(), score1.getElo());

    switch (result) {
      case R1 -> {
        score1.setElo(Math.round(score1.getElo() + K * (1 - p2)));
        score2.setElo(Math.round(score2.getElo() + K * (0 - p1)));
      }
      case R2 -> {
        score1.setElo(Math.round(score1.getElo() + K * (0 - p2)));
        score2.setElo(Math.round(score2.getElo() + K * (1 - p1)));
      } case RX -> {

      }
    }

    System.out.println("Updated Ratings:");
    System.out.println("user1: " + score1.getElo());
    System.out.println("user2: " + score2.getElo());
  }

  private float probability(float rating1, float rating2) {
    return 1.0f * 1.0f / (1 + 1.0f * (float)(Math.pow(10, 1.0f * (rating1 - rating2) / 400)));
  }
}
