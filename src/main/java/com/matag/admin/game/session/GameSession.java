package com.matag.admin.game.session;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

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
}
