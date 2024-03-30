package tz.co.yesayasoftware.yesayasoftware.models

import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int = -1,

    @field:NotNull(message = "The name field is required")
    val name: String? = null,

    @field:NotNull(message = "The email field is required")
    @field:Email(message = "The email field is invalid")
    val email: String? = null,

    @field:NotNull(message = "The password field is required")
    val password: String? = null
)