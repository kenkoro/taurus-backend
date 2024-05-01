package com.kenkoro.taurus.api.client.routes.user

import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getUser(
  controller: UserController,
  config: TokenConfig
) {
  authenticate(config.authName) {
    get("/user/{subject?}") {
      val subject = call.parameters["subject"] ?: run {
        call.respond(HttpStatusCode.BadRequest, "The user subject is null")
        return@get
      }

      val fetchedUser = controller.user(subject)
      if (fetchedUser == null) {
        call.respond(HttpStatusCode.BadRequest, "User is not found")
        return@get
      }

      call.respond(
        status = HttpStatusCode.OK,
        message = fetchedUser
      )
    }
  }
}