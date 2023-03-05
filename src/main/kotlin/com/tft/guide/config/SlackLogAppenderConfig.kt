package com.tft.guide.config

import net.gpedro.integrations.slack.SlackApi
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SlackLogAppenderConfig {

    @Value("\${slack.token}")
    lateinit var token: String

    @Bean
    fun slackApi(): SlackApi {
        return SlackApi("https://hooks.slack.com/services/$token")
    }

}