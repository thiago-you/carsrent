package br.pucpr.carsrent.security

import br.pucpr.carsrent.users.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.jackson.io.JacksonDeserializer
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Keys
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders.AUTHORIZATION
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

@Component
class Jwt {

    companion object {
        const val SECRET = "37944be5effa1578cd5447e3b5a6362701a1af03"
        const val ISSUER = "AuthServer"
        const val USER_FIELD = "User"
        const val EXPIRE_HOURS = 2L
        const val ADMIN_EXPIRE_HOURS = 2L

        private val log = LoggerFactory.getLogger(Jwt::class.java)
    }

    fun createToken(user: User): String = UserToken(user).let {
        Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(SECRET.toByteArray()))
            .serializeToJsonWith(JacksonSerializer())
            .issuedAt(utcNow().toDate())
            .expiration(utcNow().plusHours(if (it.isAdmin) ADMIN_EXPIRE_HOURS else EXPIRE_HOURS).toDate())
            .issuer(ISSUER)
            .subject(it.id.toString())
            .claim(USER_FIELD, it)
            .compact()
    }

    fun extract(req: HttpServletRequest): Authentication? {
        return try {
            val token = req.getHeader(AUTHORIZATION)?.removePrefix("Bearer ")?.trim() ?: return null

            if (token.isBlank()) {
                return null
            }

            val claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(SECRET.toByteArray()))
                .json(JacksonDeserializer(mapOf(USER_FIELD to UserToken::class.java)))
                .build()
                .parseSignedClaims(token)
                .payload

            if (claims.issuer != ISSUER) {
                return null
            }

            return claims.get("user", UserToken::class.java).toAuthentication()
        } catch (e: Exception) {
            log.debug("Token rejected", e)
            return null
        }
    }

    private fun UserToken.toAuthentication(): Authentication = roles
        .map { SimpleGrantedAuthority("ROLE_$it") }
        .let { UsernamePasswordAuthenticationToken(id, name, it) }

    private fun utcNow() = ZonedDateTime.now(ZoneOffset.UTC)

    private fun ZonedDateTime.toDate(): Date = Date.from(this.toInstant())
}