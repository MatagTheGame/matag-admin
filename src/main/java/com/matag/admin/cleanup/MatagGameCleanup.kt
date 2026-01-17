package com.matag.admin.cleanup

import com.matag.admin.game.game.Game
import com.matag.admin.game.game.GameRepository
import com.matag.admin.game.game.GameStatus
import com.matag.admin.game.session.GameSessionRepository
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.Clock
import java.time.LocalDateTime

@Component
open class MatagGameCleanup(
  private val gameRepository: GameRepository,
  private val gameSessionRepository: GameSessionRepository,
  private val clock: Clock
) {

    @Transactional
    open fun cleanup() {
        cleanupOldUnfinishedGames()
        cleanupFinishedGuestGames()
    }

    private fun cleanupOldUnfinishedGames() {
        val oldNotFinishedGames = gameRepository.findOldGameByStatus(
            listOf(
                GameStatus.STARTING,
                GameStatus.IN_PROGRESS
            ), LocalDateTime.now(clock).minusDays(2)
        )
        LOGGER.info("cleanupOldUnfinishedGames...")
        deleteGames(oldNotFinishedGames)
    }

    private fun cleanupFinishedGuestGames() {
        LOGGER.info("cleanupFinishedGuestGames...")
        val oldGuestGamesIds = gameRepository.findFinishedGuestGames(LocalDateTime.now(clock).minusDays(2))
        val oldGuestGames = oldGuestGamesIds
            .map { gameRepository.findById(it) }
            .map { it.get() }
        deleteGames(oldGuestGames)
    }

    private fun deleteGames(games: List<Game>) {
        LOGGER.info("MatagGameCleanup identified " + games.size + " entries to delete.")
        var totalGameDeleted = 0
        for (game in games) {
            try {
                for (gameSession in game.gameSessions) {
                    gameSessionRepository.delete(gameSession)
                    gameRepository.delete(game)
                    totalGameDeleted++
                }
            } catch (e: Exception) {
                LOGGER.error("Could not delete game with id " + game.id, e)
            }
        }

        LOGGER.info("MatagGameCleanup deleted " + totalGameDeleted + " games")
    }

    companion object {
        private val LOGGER: Logger = LoggerFactory.getLogger(MatagGameCleanup::class.java)
    }
}
