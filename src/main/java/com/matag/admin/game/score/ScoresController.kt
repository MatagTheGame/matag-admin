package com.matag.admin.game.score;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/game")
@AllArgsConstructor
public class ScoresController {
  private final ScoreRepository scoreRepository;

  @PreAuthorize("hasAnyRole('USER', 'GUEST')")
  @GetMapping("/scores")
  public ScoresResponse gameScore() {
    var scores = scoreRepository.findAll();
    return ScoresResponse.builder()
      .scores(toScoreResponse(scores))
      .build();
  }

  private List<ScoreResponse> toScoreResponse(List<Score> scores) {
    var scoreResponses = new ArrayList<ScoreResponse>(scores.size());
    for (int i = 0; i < scores.size(); i++) {
      var score = scores.get(i);
      scoreResponses.add(ScoreResponse.builder()
        .rank(i + 1)
        .player(score.getMatagUser().getUsername())
        .elo(score.getElo())
        .wins(score.getWins())
        .draws(score.getDraws())
        .losses(score.getLosses())
        .build());
    }
    return scoreResponses;
  }
}
