package com.tft.guide.entity.utils

import com.tft.guide.entity.Deck
import com.tft.guide.entity.IdSet
import com.tft.guide.entity.IdType
import com.tft.guide.entity.checkValidSeason


fun IdSet.Companion.of(season: String, seasonNumber: Int, type: IdType, ids: Set<String>): IdSet {
    return IdSet(
        season = season,
        seasonNumber = seasonNumber,
        type = type,
        ids = ids,
    )
}


fun IdSet.Companion.listOf(decks: Collection<Deck>): List<IdSet> =
    decks.groupBy { it.info.tft_set_core_name to it.info.tft_set_number }
        .flatMap { listOf(it.key, it.value) }


private fun IdSet.Companion.listOf(seasonPair: Pair<String, Int>, decks: Collection<Deck>): List<IdSet> {
    val (season, seasonNumber) = seasonPair

    return listOf(
        listOf(season, seasonNumber, IdType.CHAMPION, decks),
        listOf(season, seasonNumber, IdType.ITEM, decks),
        listOf(season, seasonNumber, IdType.AUGMENT, decks),
        listOf(season, seasonNumber, IdType.SYNERGY, decks),
    )
}

private fun IdSet.Companion.listOf(season: String, seasonNumber: Int, idType: IdType, decks: Collection<Deck>): IdSet {
    decks.checkValidSeason(season, seasonNumber)

    val idSet = when (idType) {
        IdType.CHAMPION -> decks.flatMap { it.units }.map { it.character_id }
        IdType.ITEM -> decks.flatMap { it.units }.flatMap { it.itemNames }
        IdType.AUGMENT -> decks.flatMap { it.augments }
        IdType.SYNERGY -> decks.flatMap { it.traits }.map { it.name }
    }.toSet()

    return IdSet(
        season = season,
        seasonNumber = seasonNumber,
        type = idType,
        ids = idSet
    )
}

fun IdSet.union(anotherIdSet: IdSet): IdSet {
    checkSameSeasonAndType(anotherIdSet)

    return this.copy(
        ids = this.ids.union(anotherIdSet.ids)
    )
}

private fun IdSet.checkSameSeasonAndType(anotherIdSet: IdSet) {
    check(this.season == anotherIdSet.season) { "different season. this:${this.season}, another:${anotherIdSet.season}" }
    check(this.seasonNumber == anotherIdSet.seasonNumber) { "different seasonNumber. this:${this.seasonNumber}, another:${anotherIdSet.seasonNumber}" }
    check(this.type == anotherIdSet.type) { "different type. this:${this.type}, another:${anotherIdSet.type}" }
}