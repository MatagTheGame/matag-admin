package com.matag.admin.stats;

import static com.matag.admin.user.MatagUserStatus.ACTIVE;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;

import com.matag.admin.user.MatagUserRepository;
import com.matag.cards.Cards;
import com.matag.cards.sets.MtgSets;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class StatsService {
  private final MatagUserRepository matagUserRepository;
  private final MtgSets mtgSets;
  private final Cards cards;
  private final Clock clock;

  public long countTotalUsers() {
    return matagUserRepository.countUsersByStatus(ACTIVE);
  }

  public List<String> onlineUsers() {
    return matagUserRepository.retrieveOnlineUsers(LocalDateTime.now(clock));
  }

  public int countCards() {
    return cards.all().size();
  }

  public int countSets() {
    return mtgSets.getSets().size();
  }
}
