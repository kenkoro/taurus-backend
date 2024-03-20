package com.kenkoro.taurus.api.client.routes.user

import com.kenkoro.taurus.api.client.core.annotations.Warning
import com.kenkoro.taurus.api.client.controllers.User
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getUser(
  user: User,
  config: TokenConfig
) {
  @Warning("Maybe you also need to check the user's role")
  authenticate(config.authName) {
    get("/api/user/@{subject?}") {
      val subject = call.parameters["subject"] ?: return@get call.respond(HttpStatusCode.BadRequest)

      val user = user.user(subject).get()

      call.respond(
        status = HttpStatusCode.OK,
        message = user
      )
    }
  }
}