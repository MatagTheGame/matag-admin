package com.matag.admin.stats;

import static com.matag.admin.user.MatagUserStatus.ACTIVE;

import com.matag.admin.session.MatagSessionRepository;
import com.matag.admin.user.MatagUserRepository;
import com.matag.cards.Cards;
import com.matag.cards.sets.MtgSets;
import java.time.Clock;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class StatsService {
  private final MatagUserRepository matagUserRepository;
  private final MatagSessionRepository matagSessionRepository;
  private final MtgSets mtgSets;
  private final Cards cards;
  private final Clock clock;

  public long countTotalUsers() {
    return matagUserRepository.countUsersByStatus(ACTIVE);
  }

  public long countOnlineUsers() {
    return matagSessionRepository.countOnlineUsers(LocalDateTime.now(clock));
  }

  public int countCards() {
    return cards.getAll().size();
  }

  public int countSets() {
    return mtgSets.getSets().size();
  }
}
