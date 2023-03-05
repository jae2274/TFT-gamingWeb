package com.tft.guide.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.mongodb.core.MongoTemplate

@SpringBootTest
class DeckRepositoryTest {
    @Autowired
    lateinit var deckRepository: DeckRepository

    @Autowired
    lateinit var mongoTemplate: MongoTemplate

    //    @Test
//    fun test() {
//        val decks = deckRepository.findByCharacterId(
//                1,
//                listOf(
//                        WinnersRequest.Unit("TFT8_Zed", 3),
//                        WinnersRequest.Unit("TFT8_Nilah", 3),
//                )
//        )
//
//        for (deck in decks) {
//            println(deck.units)
//        }
//    }
    @Test
    fun test() {

    }
}
