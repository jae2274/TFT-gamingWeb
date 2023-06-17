package com.tft.guide.controller.response

import com.tft.guide.entity.Augment

data class AugmentsRes(
        val augments: List<AugmentRes>
) {

    companion object {
        fun of(augments: List<Augment>): AugmentsRes {
            return AugmentsRes(
                    augments = AugmentRes.listOf(augments)
            )
        }
    }

    data class AugmentRes(
            val season: String,
            val name: String,
            val tier: Int,
            val tierName: String,
            val desc: String,
            val imageUrl: String,
            val engName: String,
            val dataId: String?,
    ) {
        companion object {
            fun listOf(augments: List<Augment>): List<AugmentRes> {
                return augments.map {
                    AugmentRes(
                            season = it.season,
                            name = it.name,
                            tier = it.tier,
                            tierName = it.tierName,
                            desc = it.desc,
                            imageUrl = it.imageUrl,
                            engName = it.engName,
                            dataId = it.dataId,
                    )
                }
            }
        }
    }
}


