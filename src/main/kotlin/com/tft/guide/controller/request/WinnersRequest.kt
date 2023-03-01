package com.tft.guide.controller.request


data class WinnersRequest(
        val champions: List<ChampionReq>,
        val items: List<ItemReq>,
        val replaceMatchIds: List<String>?,
) {
    data class ChampionReq(
            val dataId: String,
            val tier: Int,
    )

    data class ItemReq(
            val dataId: String,
    )
}