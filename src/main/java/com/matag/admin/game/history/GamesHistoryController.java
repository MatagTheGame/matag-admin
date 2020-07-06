package com.matag.admin.game.history;

import com.matag.admin.auth.SecurityContextHolderHelper;
import com.matag.admin.game.game.Game;
import com.matag.admin.game.game.GameRepository;
import com.matag.admin.game.result.ResultService;
import com.matag.admin.game.session.GamePlayers;
import com.matag.admin.game.session.GameSession;
import com.matag.admin.game.session.GameSessionService;
import com.matag.admin.user.MatagUser;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.matag.admin.game.game.GameStatusType.FINISHED;

@RestController
@RequestMapping("/game")
@AllArgsConstructor
public class GamesHistoryController {
  private final SecurityContextHolderHelper securityContextHolderHelper;
  private final GameRepository gameRepository;
  private final GameSessionService gameSessionService;
  private final ResultService resultService;

  @GetMapping("/history")
  public GamesHistoryResponse gameHistory() {
    var user = securityContextHolderHelper.getUser();
    var games = gameRepository.findByPlayerIdAndStatus(user.getId(), FINISHED);
    return GamesHistoryResponse.builder()
      .gamesHistory(games.stream()
        .map(game -> this.toGameHistory(game, user))
        .collect(Collectors.toList()))
      .build();
  }

  private GameHistory toGameHistory(Game game, MatagUser user) {
    var gamePlayers = gameSessionService.getGamePlayers(game);
    var player1 = gamePlayers.getPlayerSession();
    var player2 = gamePlayers.getOpponentSession();
    return GameHistory.builder()
      .gameId(game.getId())
      .startedTime(game.getCreatedAt())
      .finishedTime(game.getFinishedAt())
      .type(game.getType())
      .result(resultService.toUserResult(game, user))
      .player1Name(player1.getPlayer().getUsername())
      .player1Options(player1.getPlayerOptions())
      .player2Name(player2.getPlayer().getUsername())
      .player2Options(player2.getPlayerOptions())
      .build();
  }
}
