package tz.co.yesayasoftware.yesayasoftware.controllers.auth

import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import tz.co.yesayasoftware.yesayasoftware.dto.requests.LoginRequest
import tz.co.yesayasoftware.yesayasoftware.dto.responses.AuthenticationResponse
import tz.co.yesayasoftware.yesayasoftware.models.User
import tz.co.yesayasoftware.yesayasoftware.services.AuthService

@RestController
@Validated
@RequestMapping("api/v1/")
class AuthController(
    private val service: AuthService
) {
    @PostMapping("register")
    @ResponseStatus(HttpStatus.CREATED)
    fun register(@RequestBody @Valid user: User): User = service.register(user)

    @PostMapping("login")
    @ResponseStatus(HttpStatus.ACCEPTED)
    fun login(@RequestBody @Valid request: LoginRequest): AuthenticationResponse = service.login(request)
}