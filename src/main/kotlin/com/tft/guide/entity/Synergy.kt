package com.tft.guide.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class Synergy(
        @Id
        var _id: String? = null,
        val season: String,
        val name: String,
        val type: SynergyType,
        val desc: String,
        val stats: List<String>,
        val imageUrl: String,
        val champions: List<String>,
        override val engName: String,
        override val dataId: String,
        override val isFixed: Boolean,
        override val similarity: Double?,
) : TFTData