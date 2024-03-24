package com.kenkoro.taurus.api.client.routes.user

import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.util.UserProfile
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteUser(
  controller: UserController,
  config: TokenConfig
) {
  authenticate(config.authName) {
    delete("/delete/user/{subject?}") {
      val subject = call.parameters["subject"] ?: run {
        return@delete call.respond(HttpStatusCode.BadRequest)
      }

      /*
       * WARN: Incorrect checking
       * - You need to know who is requesting the deletion of some user,
       * not who to delete
       */
      val profile = controller
        .subject(subject)
        .read().profile
      if (profile != UserProfile.Admin) {
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

      call.respond(HttpStatusCode.OK, "Successfully deleted the user")
    }
  }
}