package tz.co.yesayasoftware.yesayasoftware.services

import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import tz.co.yesayasoftware.yesayasoftware.repositories.UserRepository

typealias ApplicationUser = tz.co.yesayasoftware.yesayasoftware.models.User

@Service
class CustomUserDetailsService(
    private val repository: UserRepository
) : UserDetailsService {
    override fun loadUserByUsername(username: String): UserDetails =
        repository.findByEmail(username)
            ?.mapToUserDetails()
            ?: throw UsernameNotFoundException("Not found")

    private fun ApplicationUser.mapToUserDetails(): UserDetails = User.builder()
        .username(this.email)
        .password(this.password)
        .build()

}