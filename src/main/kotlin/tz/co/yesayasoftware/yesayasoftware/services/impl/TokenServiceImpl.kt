package tz.co.yesayasoftware.yesayasoftware.services.impl

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Service
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.userdetails.UserDetails
import tz.co.yesayasoftware.yesayasoftware.config.JwtProperties
import tz.co.yesayasoftware.yesayasoftware.services.TokenService
import java.util.*

@Service
class TokenServiceImpl(
    properties: JwtProperties
) : TokenService {
    private val secretKey = Keys.hmacShaKeyFor(properties.key.toByteArray())

    override fun generate(
        userDetails: UserDetails,
        expirationDate: Date, additionalClaims: Map<String, Any>
    ): String = Jwts.builder()
        .claims()
        .subject(userDetails.username)
        .issuedAt(Date(System.currentTimeMillis()))
        .expiration(expirationDate)
        .add(additionalClaims)
        .and()
        .signWith(secretKey)
        .compact()

    override fun extractEmail(token: String): String? = getAllClaims(token)
        .subject

    override fun isExpired(token: String): Boolean = getAllClaims(token)
        .expiration
        .before(Date(System.currentTimeMillis()))

    override fun isValid(
        token: String,
        userDetails: UserDetails
    ): Boolean {
        val email = extractEmail(token)

        return userDetails.username == email && !isExpired(token)
    }

    private fun getAllClaims(token: String): Claims {
        val parser = Jwts.parser()
            .verifyWith(secretKey)
            .build()

        return parser
            .parseSignedClaims(token)
            .payload
    }
}