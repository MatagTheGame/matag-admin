package com.matag.admin.game.findactive;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/game")
@AllArgsConstructor
public class FindActiveGameController {
  private final FindGameService findGameService;

  @GetMapping
  public ActiveGameResponse findActiveGame() {
    return findGameService.findActiveGame();
  }
}
