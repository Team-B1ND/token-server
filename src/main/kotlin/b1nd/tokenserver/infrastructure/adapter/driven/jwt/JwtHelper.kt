package b1nd.tokenserver.infrastructure.adapter.driven.jwt

import b1nd.tokenserver.domain.auth.TokenType
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component

@Component
class JwtHelper(private val properties: JwtProperties) {

    private final val accessTokenParser = createJwtParser(properties.accessSecret)
    private final val refreshTokenParser = createJwtParser(properties.refreshSecret)

    private fun createJwtParser(secret: String): JwtParser {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.toByteArray())).build()
    }

    fun getSecretAndExpiry(type: TokenType): Pair<String, Long> {
        return when (type) {
            TokenType.ACCESS -> Pair(properties.accessSecret, properties.accessExpiryDate)
            TokenType.REFRESH -> Pair(properties.refreshSecret, properties.refreshExpiryDate)
        }
    }

    fun getParser(type: TokenType): JwtParser {
        return when (type) {
            TokenType.ACCESS -> accessTokenParser
            TokenType.REFRESH -> refreshTokenParser
        }
    }

}
