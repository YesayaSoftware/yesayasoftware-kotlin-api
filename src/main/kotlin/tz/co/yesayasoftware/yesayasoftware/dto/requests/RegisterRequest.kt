package tz.co.yesayasoftware.yesayasoftware.dto.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotNull

data class RegisterRequest(
    private val id: Int = -1,

    @field:NotNull(message = "The name field is required")
    private val name: String? = null,

    @field:NotNull(message = "The email field is required")
    @field:Email(message = "The email field is invalid")
    private val email: String? = null,

    @field:NotNull(message = "The password field is required")
    private val password: String? = null
)
