package com.tft.guide.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Champion(
        @Id
        var _id: String? = null,
        var championName: String = "",
        var cost: Int = 0,
        var traits: List<Trait> = listOf(),
        var attachRange: Int = 0,
        var skillName: String = "",
        var skillExplanation: String = "",
        var powersByLevel: List<Map<Int, PowerByLevel>> = listOf(),
        var initMana: Int = 0,
        var maxMana: Int = 0,
        var imageUrl: String = "",
        var season: String = "",
        override var isFixed: Boolean,
        override var engName: String,
        override var dataId: String,
        override var similarity: Double?,
) : TFTData {
    data class PowerByLevel(
            var effectName: String = "",
            var effectPower: String = "",
    )
}