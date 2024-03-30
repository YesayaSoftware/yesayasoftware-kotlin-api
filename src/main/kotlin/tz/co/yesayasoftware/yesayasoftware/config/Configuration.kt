package tz.co.yesayasoftware.yesayasoftware.config

import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.authentication.dao.DaoAuthenticationProvider
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import tz.co.yesayasoftware.yesayasoftware.repositories.UserRepository
import tz.co.yesayasoftware.yesayasoftware.services.CustomUserDetailsService

@Configuration
@EnableConfigurationProperties(JwtProperties::class)
class Configuration {
    @Bean
    fun userDetailsService(
        repository: UserRepository
    ): UserDetailsService = CustomUserDetailsService(repository)

    @Bean
    fun encorder(): PasswordEncoder = BCryptPasswordEncoder()

    @Bean
    fun authenticationProvider(
        repository: UserRepository
    ): AuthenticationProvider = DaoAuthenticationProvider()
        .also { provider ->
            provider.setUserDetailsService(userDetailsService(repository))
            provider.setPasswordEncoder(encorder())
        }

    @Bean
    fun authenticationManager(
        config: AuthenticationConfiguration
    ): AuthenticationManager = config.authenticationManager
}