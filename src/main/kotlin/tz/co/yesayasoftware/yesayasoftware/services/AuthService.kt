package tz.co.yesayasoftware.yesayasoftware.services

import tz.co.yesayasoftware.yesayasoftware.dto.requests.LoginRequest
import tz.co.yesayasoftware.yesayasoftware.dto.responses.AuthenticationResponse
import tz.co.yesayasoftware.yesayasoftware.models.User

interface AuthService {
    fun register(user: User): User

    fun login(request: LoginRequest) : AuthenticationResponse

    fun refresh(token: String): String?
}