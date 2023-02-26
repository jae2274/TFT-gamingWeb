package com.tft.guide.controller.response

import com.tft.guide.entity.Champion

data class ChampionsResponse(
        val champions: List<Champion>? = null
) {

    data class Champion(
            val championName: String? = null,
            val cost: Int? = null,
            val traits: List<String>? = null,
            val attachRange: Int? = null,
            val skillName: String? = null,
            val skillExplanation: String? = null,
            val powerByLevel: List<Map<Int, PowerByLevelRes>>? = null,
            val initMana: Int? = null,
            val maxMana: Int? = null,
            val imageUrl: String? = null,
            val dataId: String,
    )

    data class PowerByLevelRes(
            val effectName: String? = null,
            val effectPower: String? = null,
    ) {
        companion object {
            fun listOf(powersByLevels: List<Map<Int, com.tft.guide.entity.Champion.PowerByLevel>>): List<Map<Int, PowerByLevelRes>> {
                return powersByLevels.map { powersByLevel ->
                    powersByLevel.entries.associate { it.key to of(it.value) }
                }
            }

            fun of(powerByLevel: com.tft.guide.entity.Champion.PowerByLevel): PowerByLevelRes {
                return PowerByLevelRes(
                        effectName = powerByLevel.effectName,
                        effectPower = powerByLevel.effectPower,
                )
            }
        }
    }

    companion object {
        fun of(champions: List<com.tft.guide.entity.Champion>): ChampionsResponse {
            return ChampionsResponse(
                    champions.map { champion ->
                        Champion(
                                championName = champion.championName,
                                dataId = champion.dataId,
                                cost = champion.cost,
                                traits = champion.traits.map { it.synergyName },
                                attachRange = champion.attachRange,
                                skillName = champion.skillName,
                                skillExplanation = champion.skillExplanation,
                                powerByLevel = PowerByLevelRes.listOf(champion.powersByLevel),
                                initMana = champion.initMana,
                                maxMana = champion.maxMana,
                                imageUrl = champion.imageUrl,
                        )
                    }
            )
        }
    }

}