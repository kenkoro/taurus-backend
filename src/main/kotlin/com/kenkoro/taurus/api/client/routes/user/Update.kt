package com.kenkoro.taurus.api.client.routes.user

import com.kenkoro.taurus.api.client.services.util.UpdateType
import com.kenkoro.taurus.api.client.controllers.User
import com.kenkoro.taurus.api.client.models.request.user.Update
import com.kenkoro.taurus.api.client.models.util.UserRole
import com.kenkoro.taurus.api.client.core.security.hashing.HashingService
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.updateUserData(
  user: User,
  hashingService: HashingService,
  config: TokenConfig
) {
  authenticate(config.authName) {
    put("/api/user/@{whichUser?}/edit/{data?}") {
      val whichUser = call.parameters["whichUser"] ?: return@put call.respond(HttpStatusCode.BadRequest)
      val data = call.parameters["data"] ?: return@put call.respond(HttpStatusCode.BadRequest)
      val request = call.receiveNullable<Update>() ?: run {
        call.respond(HttpStatusCode.BadRequest)
        return@put
      }

      if (request.value.isBlank()) {
        call.respond(HttpStatusCode.Conflict, "New data are not valid")
        return@put
      }
      val role = user.user(whichUser).get().role

      val updateType = try {
        UpdateType.valueOf(data.uppercase())
      } catch (iae: IllegalArgumentException) {
        call.respond(HttpStatusCode.BadRequest, "Not a valid user's data to update")
        return@put
      }

      val wasAcknowledged = if (isPasswordUpdateType(updateType)) {
        if (!isAdminRole(role)) {
          call.respond(HttpStatusCode.Conflict, "Only admin users can change the password")
          return@put
        }

        val saltedHash = hashingService.hash(request.value)
        user
          .user(whichUser)
          .update(updateType, saltedHash.hashedPasswordWithSalt)
          .update(UpdateType.SALT, saltedHash.salt)
          .wasAcknowledged()
      } else {
        user.user(whichUser).update(updateType, request.value).wasAcknowledged()
      }

      if (!wasAcknowledged) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to push the new user's data")
        return@put
      }

      call.respond(HttpStatusCode.OK, "Successfully updated the user's data")
    }
  }
}

private fun isPasswordUpdateType(type: UpdateType): Boolean {
  return type == UpdateType.PASSWORD
}

private fun isAdminRole(role: UserRole): Boolean {
  return role == UserRole.Admin
}