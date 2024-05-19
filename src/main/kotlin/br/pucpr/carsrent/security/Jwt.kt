package br.pucpr.carsrent.security

import br.pucpr.carsrent.users.User
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.jackson.io.JacksonSerializer
import io.jsonwebtoken.security.Keys
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

    private fun utcNow() = ZonedDateTime.now(ZoneOffset.UTC)

    private fun ZonedDateTime.toDate(): Date = Date.from(this.toInstant())
}