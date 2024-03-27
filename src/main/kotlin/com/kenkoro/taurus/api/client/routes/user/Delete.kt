package com.kenkoro.taurus.api.client.routes.user

import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.request.user.DeleteUser
import com.kenkoro.taurus.api.client.models.util.UserProfile
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
      val deleter = call.receiveNullable<DeleteUser>()?.deleter ?: run {
        call.respond(HttpStatusCode.BadRequest)
        return@delete
      }
      val subject = call.parameters["subject"] ?: run {
        call.respond(HttpStatusCode.BadRequest)
        return@delete
      }

      val deleterProfile = controller
        .subject(deleter)
        .read().profile
      if (deleterProfile != UserProfile.Admin) {
        call.respond(HttpStatusCode.Conflict, "Only admin users can delete other users")
        return@delete
      }

      val wasAcknowledged = controller
        .subject(subject)
        .delete()
        .wasAcknowledged()
      if (!wasAcknowledged) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to delete the user")
        return@delete
      }

      call.respond(
        status = HttpStatusCode.OK,
        message = "Successfully deleted the user"
      )
    }
  }
}