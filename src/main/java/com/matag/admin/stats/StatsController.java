package com.matag.admin.stats;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/stats")
@AllArgsConstructor
public class StatsController {
  private final StatsService statsService;

  @GetMapping
  public StatsResponse stats() {
    return StatsResponse.builder()
      .totalUsers(statsService.countTotalUsers())
      .onlineUsers(statsService.onlineUsers())
      .totalCards(statsService.countCards())
      .totalSets(statsService.countSets())
      .build();
  }
}
