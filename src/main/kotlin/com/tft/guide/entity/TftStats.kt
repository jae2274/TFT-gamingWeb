package com.tft.guide.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document(collection = "tftStats")
data class TftStats(
        @Indexed(unique = true)
        val gameVersion: String,
        val season: String,
        val seasonNumber: Int,
        val champions: Map<String, ChampionStats> = mutableMapOf(),
        val synergies: Map<String, SynergyStats> = mutableMapOf(),
        val items: Map<String, Stats> = mutableMapOf(),
        val augments: Map<String, Stats> = mutableMapOf(),
        val createdAt: LocalDateTime,
        @Id
        var _id: String? = null,
) {
    data class ChampionStats(
            var totalPlacement: Long,
            var totalCount: Long,
            val tiers: Map<Int, Stats> = mutableMapOf(),
            val items: Map<String, Stats> = mutableMapOf(),
    )

    data class SynergyStats(
            var totalPlacement: Long,
            var totalCount: Long,
            val tiers: Map<Int, Stats> = mutableMapOf(),
    )

    data class Stats(
            var totalPlacement: Long,
            var totalCount: Long,
    )
}
