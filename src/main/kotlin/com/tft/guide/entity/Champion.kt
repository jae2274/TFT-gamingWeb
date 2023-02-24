package com.tft.guide.entity

import com.querydsl.core.annotations.QueryEntity
import org.springframework.data.mongodb.core.mapping.Document
import javax.persistence.Entity
import javax.persistence.Id

@Entity
@QueryEntity
@Document
data class Champion(
        @Id
        var _id: String? = null,
        var championName: String = "",
        var championEngName: String = "",
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
        var championId: String? = null,
        var similarity: Double = 0.0,
        var isFixed: Boolean = false,
) {
    @Entity
    @QueryEntity
    data class PowerByLevel(
            var effectName: String = "",
            var effectPower: String = "",
    )
}