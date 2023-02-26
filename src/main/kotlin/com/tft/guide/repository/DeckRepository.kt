package com.tft.guide.repository


import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.dsl.BooleanExpression
import com.tft.guide.entity.Deck
import com.tft.guide.entity.QDeck
import com.tft.guide.entity.Synergy
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.data.querydsl.QuerydslPredicateExecutor

interface DeckRepository : MongoRepository<Deck, String>, QuerydslPredicateExecutor<Deck> {
    fun findByCharacterId(characterIds: List<String>): List<Deck> {
        var where = BooleanBuilder()
        for (characterId in characterIds) {
            where = where.and(QDeck.deck.units.any().character_id.eq(characterId))
        }

        return findAll(where).take(5)
    }
//
//
//    fun findForMappingParticipants(size: Int): List<Match> {
//        val where: BooleanExpression = match.participants.isEmpty
//
//        return findAll(where, match.info.game_datetime.desc()).take(size)
//    }
}