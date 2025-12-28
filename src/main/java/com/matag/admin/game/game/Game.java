package com.matag.admin.game.game;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.matag.admin.game.session.GameSession;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

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
