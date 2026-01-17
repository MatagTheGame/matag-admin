package com.matag.admin.game.finish;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.matag.adminentities.FinishGameRequest;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/game")
@AllArgsConstructor
public class FinishGameController {
  private final FinishGameService finishGameService;

  @PostMapping("/{id}/finish")
  @PreAuthorize("hasRole('ROLE_ADMIN')")
  public void finish(@PathVariable("id") Long gameId, @RequestBody FinishGameRequest request) {
    finishGameService.finish(gameId, request);
  }
}
