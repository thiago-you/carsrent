package br.pucpr.carsrent.security

import br.pucpr.carsrent.users.Users
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
class Jwt(val properties: SecurityProperties) {

    companion object {
        const val USER_FIELD = "user"

        private val log = LoggerFactory.getLogger(Jwt::class.java)
    }

    fun createToken(user: Users): String = UserToken(user).let {
        Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(properties.secret.toByteArray()))
            .serializeToJsonWith(JacksonSerializer())
            .issuedAt(utcNow().toDate())
            .expiration(utcNow().plusHours(if (it.isAdmin) properties.expireHoursAdmin else properties.expireHours).toDate())
            .issuer(properties.issuer)
            .subject(it.id.toString())
            .claim(USER_FIELD, it)
            .compact()
    }

    fun extract(req: HttpServletRequest): Authentication? {
        try {
            val token = req.getHeader(AUTHORIZATION)?.removePrefix("Bearer ")?.trim() ?: return null

            if (token.isBlank()) {
                return null
            }

            val claims = Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(properties.secret.toByteArray()))
                .json(JacksonDeserializer(mapOf(USER_FIELD to UserToken::class.java)))
                .build()
                .parseSignedClaims(token)
                .payload

            if (claims.issuer != properties.issuer) {
                return null
            }

            return claims.get(USER_FIELD, UserToken::class.java).toAuthentication()
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