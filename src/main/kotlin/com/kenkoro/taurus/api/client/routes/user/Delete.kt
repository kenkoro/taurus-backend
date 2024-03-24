package com.kenkoro.taurus.api.client.routes.user

import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.models.request.user.DeleteUser
import com.kenkoro.taurus.api.client.models.util.UserProfile
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
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
    delete("/delete/user") {
      val request = call.receiveNullable<DeleteUser>() ?: run {
        call.respond(HttpStatusCode.BadRequest)
        return@delete
      }

      val profile = controller.subject(request.user).read().profile
      if (profile != UserProfile.Admin) {
        call.respond(HttpStatusCode.Conflict, "Only admin users can delete other users")
        return@delete
      }

      val wasAcknowledged = controller.subject(request.user).delete().wasAcknowledged()
      if (!wasAcknowledged) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to delete the user")
        return@delete
      }

      call.respond(HttpStatusCode.OK, "Successfully deleted the user")
    }
  }
}