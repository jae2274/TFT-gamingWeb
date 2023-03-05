package com.tft.guide.entity

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "synergySet")
data class SynergySet(
        @Id
        var season: String = "",
        var synergies: List<String> = listOf()
)