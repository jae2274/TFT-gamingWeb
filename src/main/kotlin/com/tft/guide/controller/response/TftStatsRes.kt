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

            fun of(stats: TftStats.ChampionStats): ChampionStatsRes {
                val subTotalCount = stats.items.entries.map { it.value.totalCount }
                    .fold(0L) { prevCount, nextCount -> prevCount + nextCount }

                val championItemStatsByCount = stats.items.entries
                    .filter { (it.value.totalPlacement / it.value.totalCount.toFloat()) < 4.5 }
                    .filter { (it.value.totalCount.toFloat() / subTotalCount) > 0.01 }
                    .sortedBy { it.value.totalPlacement / it.value.totalCount.toFloat() }
                    .take(10)

                return ChampionStatsRes(
                    totalPlacement = stats.totalPlacement,
                    totalCount = stats.totalCount,
                    tiers = StatsRes.listOf(stats.tiers.toSortedMap()),
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

            fun of(stats: TftStats.ItemStats): ItemStatsRes {
                val subTotalCount = stats.champions.entries.map { it.value.totalCount }
                    .fold(0L) { prevCount, nextCount -> prevCount + nextCount }

                val itemChampionStatsByCount = stats.champions.entries
                    .filter { (it.value.totalPlacement / it.value.totalCount.toFloat()) < 4.5 }
                    .filter { (it.value.totalCount.toFloat() / subTotalCount) > 0.01 }
                    .sortedBy { it.value.totalPlacement / it.value.totalCount.toFloat() }
                    .take(10)

                return ItemStatsRes(
                    totalPlacement = stats.totalPlacement,
                    totalCount = stats.totalCount,
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