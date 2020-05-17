package com.matag.admin.game.game;

import com.matag.admin.game.session.GameSession;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@ToString(exclude = "gameSessions")
@EqualsAndHashCode(exclude = "gameSessions")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "game")
public class Game {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private LocalDateTime createdAt;
  @Enumerated(EnumType.STRING)
  private GameType type;
  @Enumerated(EnumType.STRING)
  private GameStatusType status;
  @Enumerated(EnumType.STRING)
  private GameResultType result;
  private LocalDateTime finishedAt;

  @OneToMany(mappedBy = "game", cascade = CascadeType.REMOVE)
  private List<GameSession> gameSessions;
}
