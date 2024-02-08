package com.kenkoro.taurus.api.client.route.auth

import com.kenkoro.taurus.api.client.data.repository.UserRepository
import com.kenkoro.taurus.api.client.model.request.SignInRequest
import com.kenkoro.taurus.api.client.model.response.AuthResponse
import com.kenkoro.taurus.api.client.security.hashing.HashingService
import com.kenkoro.taurus.api.client.security.hashing.SaltedHash
import com.kenkoro.taurus.api.client.security.token.JwtTokenService
import com.kenkoro.taurus.api.client.security.token.TokenClaim
import com.kenkoro.taurus.api.client.security.token.TokenConfig
import com.kenkoro.taurus.api.client.security.token.TokenService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.login(
  userRepository: UserRepository,
  hashingService: HashingService,
  config: TokenConfig
) {
  post("/api/login") {
    val request = call.receiveNullable<SignInRequest>() ?: run {
      call.respond(HttpStatusCode.BadRequest)
      return@post
    }

    val user = userRepository.getUserByItsSubject(request.subject)

    if (!isHashedPasswordValid(request.password, SaltedHash(user.password, user.salt), hashingService)) {
      call.respond(HttpStatusCode.Conflict, "Password is not valid")
    }

    val tokenService: TokenService = JwtTokenService()
    val token = tokenService.generate(
      config = config,
      claims = arrayOf(
        TokenClaim(
          name = "sub",
          value = user.id.toString()
        ),
        TokenClaim(
          name = "iat",
          value = System.currentTimeMillis().toString()
        )
      )
    )

    call.respond(
      status = HttpStatusCode.Accepted,
      message = AuthResponse(
        token = token
      )
    )
  }
}

private fun isHashedPasswordValid(password: String, saltedHash: SaltedHash, service: HashingService): Boolean {
  return service.verify(password, saltedHash)
}