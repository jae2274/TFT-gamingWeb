package com.tft.guide.controller.request

import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.NotBlank


data class WinnersReq(
        val champions: List<ChampionReq>,
        val items: List<ItemReq>,
        val augments: List<AugmentReq>,
        val offset: Long,
        val size: Long,
) {
    data class ChampionReq(
            @field:NotBlank
            val dataId: String,
            @field:Min(1) @field:Max(1)
            val tier: Int,
            @field:Min(0) @field:Max(3)
            val itemCount: Int,
    )

    data class ItemReq(
            @field:NotBlank
            val dataId: String,
    )

    data class AugmentReq(
            @field:NotBlank
            val dataId: String,
    )
}