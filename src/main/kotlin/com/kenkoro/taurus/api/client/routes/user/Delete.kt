package com.kenkoro.taurus.api.client.routes.user

import com.kenkoro.taurus.api.client.controllers.User
import com.kenkoro.taurus.api.client.models.request.user.Delete
import com.kenkoro.taurus.api.client.models.util.UserRole
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteUser(
  user: User,
  config: TokenConfig
) {
  authenticate(config.authName) {
    delete("/api/delete/user") {
      val request = call.receiveNullable<Delete>() ?: run {
        call.respond(HttpStatusCode.BadRequest)
        return@delete
      }

      val role = user.user(request.user).get().role
      if (role != UserRole.Admin) {
        call.respond(HttpStatusCode.Conflict, "Only admin users can delete other users")
        return@delete
      }

      val wasAcknowledged = user.user(request.user).delete().wasAcknowledged()
      if (!wasAcknowledged) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to delete the user")
        return@delete
      }

      call.respond(HttpStatusCode.OK, "Successfully deleted the user")
    }
  }
}