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
import java.util.*

@Component
class JWTAdapter(val jwtProperties: JWTProperties): TokenPort {

    override fun issue(memberId: String, accessLevel: Int, type: TokenType): String {
        val secret: String
        val expiryDate: Long
        when (type) {
            TokenType.ACCESS -> {
                secret = jwtProperties.accessSecret
                expiryDate = jwtProperties.accessExpiryDate
            }

            TokenType.REFRESH -> {
                secret = jwtProperties.refreshSecret
                expiryDate = jwtProperties.refreshExpiryDate
            }
        }
        return Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(secret.toByteArray(StandardCharsets.UTF_8)), SignatureAlgorithm.HS256)
            .setSubject(type.name)
            .claim("memberId", memberId)
            .claim("accessLevel", accessLevel)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + expiryDate))
            .compact()
    }

    override fun parse(token: String, type: TokenType): Token {
        return try {
            val secret: String = when (type) {
                TokenType.ACCESS -> jwtProperties.accessSecret
                TokenType.REFRESH -> jwtProperties.refreshSecret
            }
            val claims: Jws<Claims> = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.toByteArray())).build().parseClaimsJws(token)
            Token(
                claims.body["memberId"] as String,
                claims.body["accessLevel"] as Int,
                TokenType.of(claims.body.subject)
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
