package com.tft.guide.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "characterSet")
data class CharacterSet(
        @Id
        var season: String = "",
        var characters: List<String> = listOf(),
        var isProcessed: Boolean = false
)