package com.tft.guide.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("aws")
class TFTStatsRepositoryTest {
    @Autowired
    private lateinit var tftStatsRepository: TFTStatsRepository

    @Test
    fun test() {
        val tftStats = tftStatsRepository.findTopByOrderByGameVersionDesc()

        println(tftStats)
    }
}