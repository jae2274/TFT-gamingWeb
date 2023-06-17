package com.tft.guide.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document


@Document
data class Item(
        @Id
        val _id: String,
        val itemName: String,
        val itemEffect: String,
        val itemSpec: String,
        val imageUrl: String,
        val childItems: List<String> = listOf(),
        val season: String,
        override val engName: String,
        val engName2: String,
        override var dataId: String?,
        override var isFixed: Boolean,
        override var similarity: Double?,
) : TFTData