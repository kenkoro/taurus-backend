package com.kenkoro.taurus.api.client.routes.user

import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.hashing.HashingService
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.enums.UserProfile
import com.kenkoro.taurus.api.client.routes.util.RouteService
import com.kenkoro.taurus.api.client.services.util.UserUpdateType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.updateUserData(
  controller: UserController,
  hashingService: HashingService,
  config: TokenConfig
) {
  authenticate(config.authName) {
    put("/user/{subject?}/edit/{data?}") {
      val subject = call.parameters["subject"] ?: return@put call.respond(HttpStatusCode.BadRequest)
      val (request, data) = RouteService.handleDataParameterAndGetItWithRequest(call) ?: run {
        return@put call.respond(HttpStatusCode.Conflict)
      }
      val updaterProfile = controller.subject(request.updater).read().profile

      val userUpdateType = try {
        UserUpdateType.valueOf(data.replaceFirstChar { it.uppercase() })
      } catch (iae: IllegalArgumentException) {
        call.respond(HttpStatusCode.BadRequest, "Not a valid user's data to update")
        return@put
      }

      val wasAcknowledged = if (isPasswordUpdateType(userUpdateType)) {
        if (!isAdmin(updaterProfile)) {
          call.respond(HttpStatusCode.Conflict, "Only users with admin profile can change passwords")
          return@put
        }

        val saltedHash = hashingService.hash(request.value)
        controller
          .subject(subject)
          .update(userUpdateType, saltedHash.hashedPasswordWithSalt)
          .update(UserUpdateType.Salt, saltedHash.salt)
          .wasAcknowledged()
      } else {
        controller
          .subject(subject)
          .update(userUpdateType, request.value)
          .wasAcknowledged()
      }

      if (!wasAcknowledged) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to push the new user's data")
        return@put
      }

      call.respond(
        status = HttpStatusCode.OK,
        message = "Successfully updated the user's data"
      )
    }
  }
}

private fun isPasswordUpdateType(type: UserUpdateType): Boolean {
  return type == UserUpdateType.Password
}

private fun isAdmin(profile: UserProfile): Boolean {
  return profile == UserProfile.Admin
}