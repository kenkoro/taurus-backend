package com.kenkoro.taurus.api.client.routes.user

import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.dto.DeleteDto
import com.kenkoro.taurus.api.client.models.enums.UserProfile.Admin
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteUser(
  controller: UserController,
  config: TokenConfig
) {
  authenticate(config.authName) {
    delete("/delete/user/{subject?}") {
      val deleterSubject = call.receiveNullable<DeleteDto>()?.deleterSubject ?: run {
        call.respond(HttpStatusCode.BadRequest)
        return@delete
      }
      val subject = call.parameters["subject"] ?: run {
        call.respond(HttpStatusCode.BadRequest, "The subject is null")
        return@delete
      }
      val deleterProfile = controller.user(deleterSubject)?.profile ?: run {
        call.respond(HttpStatusCode.NotFound, "The user who's deleting this user is not found")
        return@delete
      }
      if (deleterProfile != Admin) {
        call.respond(HttpStatusCode.Conflict, "Only admin users are allowed to delete users")
        return@delete
      }

      if (subject == deleterSubject) {
        call.respond(HttpStatusCode.Conflict, "The user can't delete itself")
        return@delete
      }

      val wasAcknowledged = controller.deleteUser(subject)
      if (!wasAcknowledged) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to delete the user")
        return@delete
      }

      call.respond(HttpStatusCode.OK, "Successfully deleted the user")
    }
  }
}