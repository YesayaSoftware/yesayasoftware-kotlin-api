package tz.co.yesayasoftware.yesayasoftware.dto.responses

data class AuthenticationResponse(
    val id: Int = -1,
    val name: String? = null,
    val email: String? = null,
    val token: String? = null,
    val refreshToken: String? = null
)