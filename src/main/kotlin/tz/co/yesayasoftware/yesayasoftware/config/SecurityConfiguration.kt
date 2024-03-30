package tz.co.yesayasoftware.yesayasoftware.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration(
    private val provider: AuthenticationProvider
) {
    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        filter: JwtAuthenticationFilter
    ): DefaultSecurityFilterChain = http
        .csrf { it.disable() }

        .authorizeHttpRequests {
            it.requestMatchers(
                "/api/v1/**",
                "/error"
            )
                .permitAll()
                .requestMatchers(HttpMethod.POST, "/api/user")
                .permitAll()
                .requestMatchers("/api/user**")
                .fullyAuthenticated()
        }

        .sessionManagement {
            it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        }

        .authenticationProvider(provider)

        .addFilterBefore(filter, UsernamePasswordAuthenticationFilter::class.java)

        .build()
}