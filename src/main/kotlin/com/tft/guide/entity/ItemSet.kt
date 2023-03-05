package com.tft.guide.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "itemSet")
data class ItemSet(
        @Id
        var season: String = "",
        var items: List<String> = listOf(),
        var isProcessed: Boolean = false,
)