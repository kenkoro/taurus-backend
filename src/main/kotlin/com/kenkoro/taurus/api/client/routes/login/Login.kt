package com.kenkoro.taurus.api.client.routes.login

import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.hashing.HashingService
import com.kenkoro.taurus.api.client.core.security.hashing.SaltedHash
import com.kenkoro.taurus.api.client.core.security.token.JwtTokenService
import com.kenkoro.taurus.api.client.core.security.token.TokenClaim
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.core.security.token.TokenService
import com.kenkoro.taurus.api.client.models.dto.LoginDto
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.login(
  controller: UserController,
  hashingService: HashingService,
  config: TokenConfig,
) {
  post("/login") {
    val request =
      call.receiveNullable<LoginDto>() ?: run {
        call.respond(HttpStatusCode.BadRequest)
        return@post
      }

    val user = controller.user(request.subject)
    if (user == null) {
      call.respond(HttpStatusCode.BadRequest, "User is not found")
      return@post
    }

    val saltedHash =
      SaltedHash(
        hashedPasswordWithSalt = user.password,
        salt = user.saltWrapper.salt,
      )
    if (!isHashedPasswordValid(request.password, saltedHash, hashingService)) {
      call.respond(HttpStatusCode.Conflict, "Password is not valid")
      return@post
    }

    val tokenService: TokenService = JwtTokenService()
    val token =
      tokenService.generate(
        config = config,
        claims =
          arrayOf(
            TokenClaim(
              name = "sub",
              value = user.userId.toString(),
            ),
          ),
      )

    call.respond(
      status = HttpStatusCode.Accepted,
      message = mapOf("token" to token),
    )
  }
}

private fun isHashedPasswordValid(
  password: String,
  saltedHash: SaltedHash,
  service: HashingService,
): Boolean {
  return service.verify(password, saltedHash)
}
