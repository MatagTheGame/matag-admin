package com.matag.admin.stats

import com.matag.admin.user.MatagUserRepository
import com.matag.admin.user.MatagUserStatus
import com.matag.cards.Cards
import com.matag.cards.sets.MtgSets
import lombok.AllArgsConstructor
import org.springframework.stereotype.Component
import java.time.Clock
import java.time.LocalDateTime

@Component
open class StatsService(
    private val matagUserRepository: MatagUserRepository,
    private val mtgSets: MtgSets,
    private val cards: Cards,
    private val clock: Clock
) {

    open fun countTotalUsers(): Long {
        return matagUserRepository.countUsersByStatus(MatagUserStatus.ACTIVE)
    }

    open fun onlineUsers(): List<String> {
        return matagUserRepository.retrieveOnlineUsers(LocalDateTime.now(clock))
    }

    open fun countCards(): Int {
        return cards.all().size
    }

    open fun countSets(): Int {
        return mtgSets.sets.size
    }
}
