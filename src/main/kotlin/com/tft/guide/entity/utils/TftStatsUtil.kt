package com.tft.guide.entity.utils

import com.tft.guide.entity.BaseStats
import com.tft.guide.entity.Deck
import com.tft.guide.entity.TftStats
import com.tft.guide.entity.checkValidGameVersion


fun <KEY, STATS : BaseStats> MutableMap<KEY, STATS>.addMap(another: MutableMap<KEY, STATS>) {
    another.forEach { (key, stats) ->
        this.merge(key, stats) { mine, notMine ->
            mine.add(notMine)
            mine
        }
    }
}

fun <STATS : BaseStats> STATS.add(another: STATS) {
    this.totalPlacement += another.totalPlacement
    this.totalCount += another.totalCount

    this._extensionStats.zip(another._extensionStats)
        .forEach { (myStatsMap, anotherStatsMap) ->
            myStatsMap.addMap(anotherStatsMap)
        }

}


fun TftStats.add(another: TftStats) {
    checkSameSeasonAndVersion(another)
    checkSameVersion(another)
    checkSameOrEmptyId(another)

    this.champions.addMap(another.champions)
    this.synergies.addMap(another.synergies)
    this.items.addMap(another.items)
    this.augments.addMap(another.augments)
}

private fun TftStats.checkSameSeasonAndVersion(another: TftStats) {
    check(this.season == another.season) { "different season. this:${this.season}, another:${another.season}" }
    check(this.seasonNumber == another.seasonNumber) { "different seasonNumber. this:${this.seasonNumber}, another:${another.seasonNumber}" }
}

private fun TftStats.checkSameVersion(another: TftStats) {
    check(this.gameVersion == another.gameVersion) { "different version. this:${this.gameVersion}, another:${another.gameVersion}" }
}

private fun TftStats.checkSameOrEmptyId(another: TftStats) {
    if (this._id != null && another._id != null) {
        check(this._id == another._id) { "different id. this:${this._id}, another${another._id}" }
    }
}

fun TftStats.Companion.listOf(decks: List<Deck>): List<TftStats> {
    return decks.groupBy { Triple(it.info.tft_set_core_name, it.info.tft_set_number, it.info.game_version) }
        .map {
            val (season, seasonNumber, gameVersion) = it.key
            of(season, seasonNumber, gameVersion, it.value)
        }
}

fun TftStats.Companion.of(season: String, seasonNumber: Int, gameVersion: String, decks: List<Deck>): TftStats {
    decks.checkValidGameVersion(gameVersion)

    val tftStatsList = decks.map { of(season, seasonNumber, gameVersion, it) }

    return tftStatsList.fold(TftStats(gameVersion, season, seasonNumber))
    { result, tftStats ->
        result.add(tftStats)
        result
    }
}

fun TftStats.Companion.of(season: String, seasonNumber: Int, gameVersion: String, deck: Deck): TftStats {
    return TftStats(
        season = season,
        seasonNumber = seasonNumber,
        gameVersion = gameVersion,
        champions = TftStats.ChampionStats.mapOf(deck),
        items = TftStats.ItemStats.mapOf(deck),
        synergies = TftStats.SynergyStats.mapOf(deck),
        augments = TftStats.Stats.mapOf(deck.placement.toLong(), deck.augments)
    )
}


private fun TftStats.ChampionStats.Companion.mapOf(deck: Deck): MutableMap<String, TftStats.ChampionStats> {
    val placement = deck.placement.toLong()

    val result = mutableMapOf<String, TftStats.ChampionStats>()

    for (unit in deck.units) {
        val championStats = result.applyPlacement(unit.character_id, placement, TftStats.ChampionStats())
        championStats.tiers.applyPlacement(unit.tier, placement, TftStats.Stats())

        for (itemName in unit.itemNames) {
            championStats.items.applyPlacement(itemName, placement, TftStats.Stats())
        }
    }

    return result
}


private fun TftStats.ItemStats.Companion.mapOf(deck: Deck): MutableMap<String, TftStats.ItemStats> {
    val placement = deck.placement.toLong()

    val result = mutableMapOf<String, TftStats.ItemStats>()

    for (unit in deck.units) {
        for (itemName in unit.itemNames) {
            val itemStats = result.applyPlacement(itemName, placement, TftStats.ItemStats())

            itemStats.champions.applyPlacement(unit.character_id, placement, TftStats.Stats())
        }
    }

    return result
}


private fun TftStats.SynergyStats.Companion.mapOf(deck: Deck): MutableMap<String, TftStats.SynergyStats> {
    val placement = deck.placement.toLong()
    val result = mutableMapOf<String, TftStats.SynergyStats>()

    for (trait in deck.traits) {
        val synergyStats =
            if (trait.tier_current != 0) {
                result.applyPlacement(trait.name, placement, TftStats.SynergyStats())
            } else {
                result.getOrPut(trait.name) { TftStats.SynergyStats() }
            }

        synergyStats.tiers.applyPlacement(trait.tier_current, placement, TftStats.Stats())
    }

    return result
}


private fun <KEY> TftStats.Stats.Companion.mapOf(
    placement: Long,
    keys: List<KEY>,
): MutableMap<KEY, TftStats.Stats> {
    val result = mutableMapOf<KEY, TftStats.Stats>()

    keys
        .map { key -> result.applyPlacement(key, placement, TftStats.Stats()) }

    return result
}


private fun BaseStats.applyPlacement(placement: Long) {
    this.totalPlacement += placement
    this.totalCount++
}

private fun <KEY, STATS : BaseStats> MutableMap<KEY, STATS>.applyPlacement(
    key: KEY,
    placement: Long,
    default: STATS
): STATS {
    return this.getOrPut(key) { default }
        .also { it.applyPlacement(placement) }
}