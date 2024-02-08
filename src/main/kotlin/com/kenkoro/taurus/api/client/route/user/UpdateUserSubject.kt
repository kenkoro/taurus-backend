package com.kenkoro.taurus.api.client.route.user

import com.kenkoro.taurus.api.client.data.repository.UserRepository
import com.kenkoro.taurus.api.client.model.request.UpdateUserFieldExceptRoleRequest
import com.kenkoro.taurus.api.client.model.util.UserRole
import com.kenkoro.taurus.api.client.security.token.TokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.updateUserSubject(
  userRepository: UserRepository,
  config: TokenConfig
) {
  authenticate(config.authName) {
    put("/api/user/edit/subject/{whichUser?}") {
      val whichUser = call.parameters["whichUser"] ?: return@put call.respond(HttpStatusCode.BadRequest)
      val request = call.receiveNullable<UpdateUserFieldExceptRoleRequest>() ?: run {
        call.respond(HttpStatusCode.BadRequest)
        return@put
      }

      if (request.value.isBlank()) {
        call.respond(HttpStatusCode.Conflict, "New subject is not valid")
        return@put
      }

      val userRole = userRepository.user(whichUser).get().role
      if (!isAdmin(userRole)) {
        call.respond(HttpStatusCode.Conflict, "Only users with admin role can change a user's subject")
        return@put
      }

      val wasAcknowledged = userRepository.user(whichUser).updateSubject(request.value)
      if (!wasAcknowledged) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to push the new user's subject")
        return@put
      }

      call.respond(HttpStatusCode.OK, "Successfully updated the user's subject")
    }
  }
}

private fun isAdmin(userRole: UserRole): Boolean {
  return userRole == UserRole.ADMIN
}