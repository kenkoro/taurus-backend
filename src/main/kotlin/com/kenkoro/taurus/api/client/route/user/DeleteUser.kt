package com.kenkoro.taurus.api.client.route.user

import com.kenkoro.taurus.api.client.data.repository.UserRepository
import com.kenkoro.taurus.api.client.model.request.DeleteUser
import com.kenkoro.taurus.api.client.model.util.UserRole
import com.kenkoro.taurus.api.client.security.token.TokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteUser(
  userRepository: UserRepository,
  config: TokenConfig
) {
  authenticate(config.authName) {
    delete("/api/delete/user") {
      val request = call.receiveNullable<DeleteUser>() ?: run {
        call.respond(HttpStatusCode.BadRequest)
        return@delete
      }

      val role = userRepository.user(request.user).get().role
      if (role != UserRole.ADMIN) {
        call.respond(HttpStatusCode.Conflict, "Only admin users can delete other users")
        return@delete
      }

      val wasAcknowledged = userRepository.user(request.user).delete().wasAcknowledged()
      if (!wasAcknowledged) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to delete the user")
        return@delete
      }

      call.respond(HttpStatusCode.OK, "Successfully deleted the user")
    }
  }
}