package com.matag.admin.game.score;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/game")
@AllArgsConstructor
public class GameScoresController {
  @GetMapping("/scores")
  public GameScoresResponse gameHistory() {
    return GameScoresResponse.builder()
      .gameScores(List.of(GameScore.builder()
        .rank(1L)
        .player("Foo")
        .elo(1400)
        .wins(4L)
        .draws(1L)
        .losses(2L)
        .build())
      )
      .build();
  }
}
