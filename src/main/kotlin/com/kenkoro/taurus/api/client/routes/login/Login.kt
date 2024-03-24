package com.kenkoro.taurus.api.client.routes.login

import com.kenkoro.taurus.api.client.controllers.User
import com.kenkoro.taurus.api.client.core.security.hashing.HashingService
import com.kenkoro.taurus.api.client.core.security.hashing.SaltedHash
import com.kenkoro.taurus.api.client.core.security.token.JwtTokenService
import com.kenkoro.taurus.api.client.core.security.token.TokenClaim
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.core.security.token.TokenService
import com.kenkoro.taurus.api.client.models.request.login.LoginRequest
import com.kenkoro.taurus.api.client.models.response.LoginResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.login(
  controller: User,
  hashingService: HashingService,
  config: TokenConfig
) {
  post("/login") {
    val request = call.receiveNullable<LoginRequest>() ?: run {
      call.respond(HttpStatusCode.BadRequest)
      return@post
    }

    val fetchedUser = controller.subject(request.subject).get()

    if (!isHashedPasswordValid(request.password, SaltedHash(fetchedUser.password, fetchedUser.salt), hashingService)) {
      call.respond(HttpStatusCode.Conflict, "Password is not valid")
      return@post
    }

    val tokenService: TokenService = JwtTokenService()
    val token = tokenService.generate(
      config = config,
      claims = arrayOf(
        TokenClaim(
          name = "sub",
          value = fetchedUser.id.toString()
        )
      )
    )

    call.respond(
      status = HttpStatusCode.Accepted,
      message = LoginResponse(
        token = token
      )
    )
  }
}

private fun isHashedPasswordValid(password: String, saltedHash: SaltedHash, service: HashingService): Boolean {
  return service.verify(password, saltedHash)
}