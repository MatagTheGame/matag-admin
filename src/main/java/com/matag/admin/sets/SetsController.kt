package com.matag.admin.sets

import com.matag.cards.sets.MtgSets
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/sets")
open class SetsController(
    @param:Autowired val mtgSets: MtgSets
) {
    @PreAuthorize("permitAll()")
    @GetMapping
    open fun sets(): MtgSetsResponse =
        mtgSets.sets
            .map { MtgSet(it.value.code, it.value.name) }
            .let { MtgSetsResponse(it) }
}

data class MtgSet(
    var code: String = "",
    var name: String = ""
)

data class MtgSetsResponse(
    var sets: List<MtgSet> = listOf()
)
