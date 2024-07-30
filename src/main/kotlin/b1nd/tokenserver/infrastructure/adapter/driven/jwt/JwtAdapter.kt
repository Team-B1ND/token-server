package b1nd.tokenserver.infrastructure.adapter.driven.jwt

import b1nd.tokenserver.application.auth.outport.TokenPort
import b1nd.tokenserver.domain.auth.exception.EmptyTokenException
import b1nd.tokenserver.domain.auth.exception.ExpiredTokenException
import b1nd.tokenserver.domain.auth.exception.InvalidTokenException
import b1nd.tokenserver.domain.auth.TokenType
import b1nd.tokenserver.domain.auth.Token
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.nio.charset.StandardCharsets
import java.security.SecureRandom
import java.util.*

@Component
class JwtAdapter(private val helper: JwtHelper): TokenPort {

    private val secureRandom = SecureRandom.getInstance("NativePRNGNonBlocking")

    override fun issue(memberId: String, accessLevel: Int, type: TokenType): String {
        val (secret, expiryDate) = helper.getSecretAndExpiry(type)
        return Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8)), Jwts.SIG.HS256)
            .subject(type.name)
            .claim("memberId", memberId)
            .claim("accessLevel", accessLevel)
            .issuedAt(Date())
            .expiration(Date(System.currentTimeMillis() + expiryDate))
            .apply {
                random(secureRandom)
            }
            .compact()
    }

    override fun parse(token: String, type: TokenType): Token {
        return try {
            val claims: Jws<Claims> = helper.getParser(type).parseSignedClaims(token)
            Token(
                claims.payload["memberId"] as String,
                claims.payload["accessLevel"] as Int,
                TokenType.of(claims.payload.subject)
            )
        } catch (e: Exception) {
            when (e) {
                is ExpiredJwtException -> throw ExpiredTokenException
                is JwtException -> throw InvalidTokenException
                is IllegalArgumentException -> throw EmptyTokenException
                else -> throw InvalidTokenException
            }
        }
    }

}
