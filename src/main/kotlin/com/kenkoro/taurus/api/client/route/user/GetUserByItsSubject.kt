package com.kenkoro.taurus.api.client.route.user

import com.kenkoro.taurus.api.client.annotation.Warning
import com.kenkoro.taurus.api.client.data.repository.UserRepository
import com.kenkoro.taurus.api.client.security.token.TokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getUserByItsSubject(
  userRepository: UserRepository,
  config: TokenConfig
) {
  @Warning("Maybe you also need to check the user's role")
  authenticate(config.authName) {
    get("/api/user/{subject?}") {
      val subject = call.parameters["subject"] ?: return@get call.respond(HttpStatusCode.BadRequest)

      val user = userRepository.user(subject).get()

      call.respond(
        status = HttpStatusCode.OK,
        message = user
      )
    }
  }
}