package com.tft.guide.config

import com.mongodb.MongoClientSettings
import com.mongodb.MongoCredential
import com.mongodb.ServerAddress
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration


@ConfigurationProperties(prefix = "spring.data.mongodb") //application.yml 값 가져오기
@Configuration
class MongoConfig : AbstractMongoClientConfiguration() {
    lateinit var database: String
    lateinit var host: String
    lateinit var port: String
    var username: String? = null
    var password: String? = null

    override fun mongoClientSettings(): MongoClientSettings {
        val superSettings = super.mongoClientSettings()

        val settings = MongoClientSettings.builder(superSettings)
            .applyToClusterSettings { it.hosts(listOf(ServerAddress(host, port.toInt()))) }
            .let {
                if (username != null && password != null) {
                    it.credential(
                        MongoCredential.createCredential(
                            username!!,
                            database,
                            password!!.toCharArray()
                        )
                    )
                } else {
                    it
                }
            }.build()

        return settings
    }

    override fun getDatabaseName(): String {
        return database
    }

    // rest of the config goes here
    override fun autoIndexCreation(): Boolean {
        return true
    }
}