package com.tft.guide.controller.response

import com.tft.guide.entity.Item

data class ItemsRes(
        val items: List<ItemRes>
) {
    data class ItemRes(
            var _id: String,
            var itemName: String,
            var itemEngName: String,
            var itemEffect: String,
            var itemSpec: String,
            var imageUrl: String,
            var childItems: List<String> = listOf(),
            var season: String,
            var dataId: String,
    ) {
        companion object {
            fun listOf(items: List<Item>): List<ItemRes> {
                return items.map { of(it) }
            }

            fun of(item: Item): ItemRes {
                return ItemRes(
                        _id = item._id,
                        itemName = item.itemName,
                        itemEngName = item.engName,
                        itemEffect = item.itemEffect,
                        itemSpec = item.itemSpec,
                        imageUrl = item.imageUrl,
                        childItems = item.childItems,
                        season = item.season,
                        dataId = item.dataId,
                )
            }
        }
    }

    companion object {
        fun of(items: List<Item>): ItemsRes {
            return ItemsRes(
                    items = ItemRes.listOf(items)
            )
        }
    }
}
