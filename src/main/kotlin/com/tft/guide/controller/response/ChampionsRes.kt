package com.tft.guide.controller.response

data class ChampionsRes(
        val champions: List<Champion>? = null
) {

    data class Champion(
            val name: String,
            val engName: String,
            val cost: Int,
            val traits: List<String>,
            val attachRange: Int,
            val skillName: String,
            val skillExplanation: String,
            val powerByLevel: List<Map<Int, PowerByLevelRes>>,
            val initMana: Int,
            val maxMana: Int,
            val imageUrl: String,
            val dataId: String,
            val season: String,
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
        fun of(champions: List<com.tft.guide.entity.Champion>): ChampionsRes {
            return ChampionsRes(
                    champions.map { champion ->
                        Champion(
                                name = champion.championName,
                                engName = champion.engName,
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
                                season = champion.season
                        )
                    }
            )
        }
    }

}