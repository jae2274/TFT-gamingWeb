package com.tft.guide.controller.response

import com.tft.guide.entity.TftStats
import java.time.LocalDateTime

data class TftStatsRes(
    val gameVersion: String,
    val season: String,
    val seasonNumber: Int,
    val champions: Map<String, ChampionStatsRes>,
    val items: Map<String, ItemStatsRes>,
    val synergies: Map<String, SynergyStatsRes>,
    val augments: List<StatsRes<String>>,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun of(tftStats: TftStats): TftStatsRes {
            return TftStatsRes(
                gameVersion = tftStats.gameVersion,
                season = tftStats.season,
                seasonNumber = tftStats.seasonNumber,
                champions = ChampionStatsRes.mapOf(tftStats.champions),
                items = ItemStatsRes.mapOf(tftStats.items),
                synergies = SynergyStatsRes.mapOf(tftStats.synergies),
                augments = StatsRes.listOf(tftStats.augments),
                createdAt = tftStats.createdAt,
            )
        }
    }

    data class ChampionStatsRes(
        var totalPlacement: Long = 0,
        var totalCount: Long = 0,
        val tiers: List<StatsRes<Int>> = mutableListOf(),
        val itemsSortedByCount: List<StatsRes<String>> = mutableListOf(),
    ) {
        companion object {
            fun mapOf(champions: Map<String, TftStats.ChampionStats>): Map<String, ChampionStatsRes> {
                return champions.entries.associateBy({ it.key }, { ChampionStatsRes.of(it.value) })
            }

            fun of(championStats: TftStats.ChampionStats): ChampionStatsRes {

                val championItemStatsByCount = championStats.items.entries
                    .sortedByDescending { it.value.totalCount }
                    .filter { (it.value.totalPlacement / it.value.totalCount.toFloat()) < 4.5 }
                    .take(10)
                    .sortedBy { it.value.totalPlacement / it.value.totalCount.toFloat() }

                return ChampionStatsRes(
                    totalPlacement = championStats.totalPlacement,
                    totalCount = championStats.totalCount,
                    tiers = StatsRes.listOf(championStats.tiers.toSortedMap()),
                    itemsSortedByCount = StatsRes.listOf(championItemStatsByCount),
                )
            }
        }
    }

    data class ItemStatsRes(
        var totalPlacement: Long = 0,
        var totalCount: Long = 0,
        val championsSortedByCount: List<StatsRes<String>> = mutableListOf(),
    ) {
        companion object {
            fun mapOf(items: Map<String, TftStats.ItemStats>): Map<String, ItemStatsRes> {
                return items.entries.associateBy({ it.key }, { of(it.value) })
            }

            fun of(itemStats: TftStats.ItemStats): ItemStatsRes {

                val itemChampionStatsByCount = itemStats.champions.entries
                    .sortedByDescending { it.value.totalCount }
                    .filter { (it.value.totalPlacement / it.value.totalCount.toFloat()) < 4.5 }
                    .take(10)
                    .sortedBy { it.value.totalPlacement / it.value.totalCount.toFloat() }

                return ItemStatsRes(
                    totalPlacement = itemStats.totalPlacement,
                    totalCount = itemStats.totalCount,
                    championsSortedByCount = StatsRes.listOf(itemChampionStatsByCount),
                )
            }
        }
    }

    data class SynergyStatsRes(
        var totalPlacement: Long,
        var totalCount: Long,
        val tiers: List<StatsRes<Int>>,
    ) {
        companion object {
            fun mapOf(synergies: Map<String, TftStats.SynergyStats>): Map<String, SynergyStatsRes> {
                return synergies.entries.associateBy({ it.key }, { SynergyStatsRes.of(it.value) })
            }

            fun of(synergy: TftStats.SynergyStats): SynergyStatsRes {
                return SynergyStatsRes(
                    totalPlacement = synergy.totalPlacement,
                    totalCount = synergy.totalCount,
                    tiers = StatsRes.listOf(synergy.tiers.toSortedMap()),
                )
            }
        }
    }

    data class StatsRes<T>(
        var key: T,
        var totalPlacement: Long,
        var totalCount: Long,
    ) {
        companion object {
            fun <T> listOf(
                statsMap: Map<T, TftStats.Stats>
            ): List<StatsRes<T>> {
                return listOf(statsMap.entries)
            }


            fun <T> listOf(statsMapEntries: Collection<Map.Entry<T, TftStats.Stats>>): List<StatsRes<T>> {
                return statsMapEntries.map { of(it.key, it.value) }
            }

            fun <T> of(key: T, stats: TftStats.Stats): StatsRes<T> {
                return StatsRes(
                    key = key,
                    totalPlacement = stats.totalPlacement,
                    totalCount = stats.totalCount,
                )
            }
        }
    }
}