package com.tft.guide.controller.request


data class WinnersRequest(
        val champions: List<ChampionReq>,
        val items: List<ItemReq>,
        val augments: List<AugmentReq>,
        val offset: Long,
        val size: Long,
) {
    data class ChampionReq(
            val dataId: String,
            val tier: Int,
    )

    data class ItemReq(
            val dataId: String,
    )

    data class AugmentReq(
            val dataId: String,
    )
}