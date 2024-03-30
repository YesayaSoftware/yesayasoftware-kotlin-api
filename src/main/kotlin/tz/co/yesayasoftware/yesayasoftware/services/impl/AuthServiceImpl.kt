package tz.co.yesayasoftware.yesayasoftware.services.impl

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import tz.co.yesayasoftware.yesayasoftware.config.JwtProperties
import tz.co.yesayasoftware.yesayasoftware.dto.requests.LoginRequest
import tz.co.yesayasoftware.yesayasoftware.dto.responses.AuthenticationResponse
import tz.co.yesayasoftware.yesayasoftware.models.User
import tz.co.yesayasoftware.yesayasoftware.repositories.RefreshTokenRepository
import tz.co.yesayasoftware.yesayasoftware.repositories.UserRepository
import tz.co.yesayasoftware.yesayasoftware.services.AuthService
import tz.co.yesayasoftware.yesayasoftware.services.CustomUserDetailsService
import tz.co.yesayasoftware.yesayasoftware.services.TokenService
import java.util.*

@Service
class AuthServiceImpl(
    private val manager: AuthenticationManager,
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService,
    private val properties: JwtProperties,
    private val refreshTokenRepository: RefreshTokenRepository,
    private val userRepository: UserRepository,
    private val encoder: PasswordEncoder
) : AuthService {
    override fun register(user: User): User = userRepository.save(user)
    override fun login(request: LoginRequest): AuthenticationResponse {
        manager.authenticate(
            UsernamePasswordAuthenticationToken(
                request.email,
                request.password
            )
        )

        val user = userDetailsService.loadUserByUsername(request.email!!)
        val userInfo = userRepository.findByEmail(request.email)

        val accessToken = generateAccessToken(user)

        val refreshToken = generateRefreshToken(user)

        refreshTokenRepository.save(refreshToken, user)

        return AuthenticationResponse(
            id = userInfo!!.id,
            name = userInfo.name,
            email = user.username,
            token = accessToken,
            refreshToken = refreshToken,
        )
    }

    private fun generateRefreshToken(user: UserDetails) = tokenService.generate(
        userDetails = user,
        expirationDate = Date(System.currentTimeMillis() + properties.refreshTokenExpiration)
    )

    private fun generateAccessToken(user: UserDetails) = tokenService.generate(
        userDetails = user,
        expirationDate = Date(System.currentTimeMillis() + properties.accessTokenExpiration)
    )

    override fun refresh(token: String): String? {
        val extractedEmail = tokenService.extractEmail(token)

        return extractedEmail?.let { email ->
            val currentUserDetails = userDetailsService.loadUserByUsername(email)

            val refreshTokenUserDetails = refreshTokenRepository.findUserDetailsByToken(token)

            if (!tokenService.isExpired(token) && currentUserDetails.username == refreshTokenUserDetails?.username)
                generateAccessToken(currentUserDetails)
            else null

        }
    }
}