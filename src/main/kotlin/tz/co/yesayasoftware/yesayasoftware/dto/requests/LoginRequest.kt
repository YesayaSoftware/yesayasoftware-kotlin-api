package tz.co.yesayasoftware.yesayasoftware.dto.requests

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class LoginRequest(
    @field:NotNull(message = "The email field is required")
    @field:NotBlank(message = "The email field is required")
    @field:Email(message = "The email field is invalid")
    val email: String? = null,

    @field:NotNull(message = "The password field is required")
    @field:NotBlank(message = "The password field is required")
    val password: String? = null
)
