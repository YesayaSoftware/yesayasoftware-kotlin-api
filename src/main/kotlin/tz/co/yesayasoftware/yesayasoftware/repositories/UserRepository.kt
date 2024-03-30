package tz.co.yesayasoftware.yesayasoftware.repositories

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import tz.co.yesayasoftware.yesayasoftware.models.User

@Repository
interface UserRepository : JpaRepository<User, Int> {
    fun findByEmail(email: String): User?
}