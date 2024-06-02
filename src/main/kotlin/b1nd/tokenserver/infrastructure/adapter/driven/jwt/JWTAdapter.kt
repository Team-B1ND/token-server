package b1nd.tokenserver.infrastructure.adapter.driven.jwt

import b1nd.tokenserver.domain.auth.outport.TokenPort
import b1nd.tokenserver.domain.auth.core.exception.EmptyTokenException
import b1nd.tokenserver.domain.auth.core.exception.ExpiredTokenException
import b1nd.tokenserver.domain.auth.core.exception.InvalidTokenException
import b1nd.tokenserver.domain.auth.core.JWTType
import b1nd.tokenserver.domain.auth.core.Token
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.lang.IllegalArgumentException
import java.nio.charset.StandardCharsets
import java.util.*

@Component
class JWTAdapter(val jwtProperties: JWTProperties): TokenPort {

    override fun issue(memberId: String, accessLevel: Int, type: JWTType): String {
        val secret: String
        val expiryDate: Long
        when (type) {
            JWTType.ACCESS -> {
                secret = jwtProperties.accessSecret
                expiryDate = jwtProperties.accessExpiryDate
            }

            JWTType.REFRESH -> {
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

    override fun parse(token: String, type: JWTType): Token {
        return try {
            val secret: String = when (type) {
                JWTType.ACCESS -> jwtProperties.accessSecret
                JWTType.REFRESH -> jwtProperties.refreshSecret
            }
            val claims: Jws<Claims> = Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secret.toByteArray())).build().parseClaimsJws(token)
            Token(
                claims.body["memberId"] as String,
                claims.body["accessLevel"] as Int,
                JWTType.of(claims.body.subject)
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
