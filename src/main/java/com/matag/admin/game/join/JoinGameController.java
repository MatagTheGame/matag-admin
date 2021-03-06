package com.matag.admin.game.join;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/game")
@AllArgsConstructor
public class JoinGameController {
  private final JoinGameService joinGameService;

  @PostMapping
  public JoinGameResponse joinGame(@RequestBody JoinGameRequest joinGameRequest) {
    return joinGameService.joinGame(joinGameRequest);
  }
}
