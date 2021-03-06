package com.matag.admin.game.cancel;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/game")
@AllArgsConstructor
public class CancelGameController {
  private final CancelGameService cancelGameService;

  @DeleteMapping("/{id}")
  public boolean cancelGame(@PathVariable("id") Long gameId) {
    cancelGameService.cancel(gameId);
    return true;
  }
}
