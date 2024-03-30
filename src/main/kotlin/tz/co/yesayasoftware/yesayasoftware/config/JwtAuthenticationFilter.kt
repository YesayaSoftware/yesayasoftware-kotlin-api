package tz.co.yesayasoftware.yesayasoftware.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import tz.co.yesayasoftware.yesayasoftware.services.CustomUserDetailsService
import tz.co.yesayasoftware.yesayasoftware.services.TokenService

@Component
class JwtAuthenticationFilter(
    private val userDetailsService: CustomUserDetailsService,
    private val tokenService: TokenService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val header: String? = request.getHeader("Authorization")

        if (header.doesNotContainBearerToken()) {
            filterChain.doFilter(request, response)

            return
        }

        val token = header!!.extractTokenValue()

        val email = tokenService.extractEmail(token)

        if(email != null && SecurityContextHolder.getContext().authentication == null) {
            val user = userDetailsService.loadUserByUsername(email)

            if (tokenService.isValid(token, user))
                updateContext(user, request)

            filterChain.doFilter(request, response)
        }
    }

    private fun updateContext(
        user: UserDetails,
        request: HttpServletRequest
    ) {
        val token = UsernamePasswordAuthenticationToken(user, null, user.authorities)

        token.details = WebAuthenticationDetailsSource().buildDetails(request)

        SecurityContextHolder.getContext().authentication = token
    }

    private fun String?.doesNotContainBearerToken(): Boolean = this == null || !this.startsWith("Bearer ")

    private fun String.extractTokenValue(): String = this.substringAfter("Bearer ")
}