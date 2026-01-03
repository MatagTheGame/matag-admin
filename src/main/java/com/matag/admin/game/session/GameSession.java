package com.matag.admin.game.session;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import com.matag.admin.game.game.Game;
import com.matag.admin.session.MatagSession;
import com.matag.admin.user.MatagUser;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "game_session")
public class GameSession {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private Game game;
  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private MatagSession session;
  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private MatagUser player;
  private String playerOptions;

  public Long getId() {
    return id;
  }

  public Game getGame() {
    return game;
  }

  public MatagSession getSession() {
    return session;
  }

  public MatagUser getPlayer() {
    return player;
  }

  public String getPlayerOptions() {
    return playerOptions;
  }
}
