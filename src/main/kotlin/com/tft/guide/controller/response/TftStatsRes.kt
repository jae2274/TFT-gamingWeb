package com.tft.guide.controller.response

import com.tft.guide.entity.TftStats
import java.time.LocalDateTime

data class TftStatsRes(
        val gameVersion: String,
        val season: String,
        val seasonNumber: Int,
        val champions: Map<String, ChampionStatsRes>,
        val synergies: Map<String, SynergyStatsRes>,
        val augments: Map<String, StatsRes>,
        val createdAt: LocalDateTime,
) {
    companion object {
        fun of(tftStats: TftStats): TftStatsRes {
            return TftStatsRes(
                    gameVersion = tftStats.gameVersion,
                    season = tftStats.season,
                    seasonNumber = tftStats.seasonNumber,
                    champions = ChampionStatsRes.mapOf(tftStats.champions),
                    synergies = SynergyStatsRes.mapOf(tftStats.synergies),
                    augments = StatsRes.mapStringOf(tftStats.augments),
                    createdAt = tftStats.createdAt,
            )
        }
    }

    data class ChampionStatsRes(
            var totalPlacement: Long = 0,
            var totalCount: Long = 0,
            val tiers: Map<Int, StatsRes> = mutableMapOf(),
            val items: Map<String, StatsRes> = mutableMapOf(),
    ) {
        companion object {
            fun mapOf(champions: Map<String, TftStats.ChampionStats>): Map<String, ChampionStatsRes> {
                return champions.entries.associateBy({ it.key }, { ChampionStatsRes.of(it.value) })
            }

            fun of(championStats: TftStats.ChampionStats): ChampionStatsRes {
                return ChampionStatsRes(
                        totalPlacement = championStats.totalPlacement,
                        totalCount = championStats.totalCount,
                        tiers = StatsRes.mapIntOf(championStats.tiers)
                )
            }
        }
    }

    data class SynergyStatsRes(
            var totalPlacement: Long,
            var totalCount: Long,
            val tiers: Map<Int, StatsRes>,
    ) {
        companion object {
            fun mapOf(synergies: Map<String, TftStats.SynergyStats>): Map<String, SynergyStatsRes> {
                return synergies.entries.associateBy({ it.key }, { SynergyStatsRes.of(it.value) })
            }

            fun of(synergy: TftStats.SynergyStats): SynergyStatsRes {
                return SynergyStatsRes(
                        totalPlacement = synergy.totalPlacement,
                        totalCount = synergy.totalCount,
                        tiers = StatsRes.mapIntOf(synergy.tiers),
                )
            }
        }
    }

    data class StatsRes(
            var totalPlacement: Long,
            var totalCount: Long,
    ) {
        companion object {

            fun of(stats: TftStats.Stats): StatsRes {
                return StatsRes(
                        totalPlacement = stats.totalPlacement,
                        totalCount = stats.totalCount,
                )
            }

            fun mapStringOf(statsMap: Map<String, TftStats.Stats>): Map<String, StatsRes> {
                return statsMap.entries.associateBy({ it.key }, { StatsRes.of(it.value) })
            }

            fun mapIntOf(statsMap: Map<Int, TftStats.Stats>): Map<Int, StatsRes> {
                return statsMap.entries.associateBy({ it.key }, { StatsRes.of(it.value) })
            }
        }
    }
}