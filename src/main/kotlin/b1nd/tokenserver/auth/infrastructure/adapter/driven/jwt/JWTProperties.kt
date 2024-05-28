package b1nd.tokenserver.auth.infrastructure.adapter.driven.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JWTProperties(

    val accessSecret: String,
    val refreshSecret: String,
    val accessExpiryDate: Long,
    val refreshExpiryDate: Long

)
