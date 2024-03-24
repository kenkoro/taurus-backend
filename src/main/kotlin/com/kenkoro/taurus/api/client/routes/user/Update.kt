package com.kenkoro.taurus.api.client.routes.user

import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.hashing.HashingService
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.request.user.UpdateUser
import com.kenkoro.taurus.api.client.models.util.UserProfile
import com.kenkoro.taurus.api.client.services.util.UserUpdateType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.updateUserData(
  controller: UserController,
  hashingService: HashingService,
  config: TokenConfig
) {
  authenticate(config.authName) {
    put("/user/@{subject?}/edit/{data?}") {
      val subject = call.parameters["subject"] ?: return@put call.respond(HttpStatusCode.BadRequest)
      val data = call.parameters["data"] ?: return@put call.respond(HttpStatusCode.BadRequest)
      val request = call.receiveNullable<UpdateUser>() ?: run {
        call.respond(HttpStatusCode.BadRequest)
        return@put
      }

      if (request.value.isBlank()) {
        call.respond(HttpStatusCode.Conflict, "New data are not valid")
        return@put
      }
      val profile = controller.subject(subject).read().profile

      val userUpdateType = try {
        UserUpdateType.valueOf(data.uppercase())
      } catch (iae: IllegalArgumentException) {
        call.respond(HttpStatusCode.BadRequest, "Not a valid user's data to update")
        return@put
      }

      val wasAcknowledged = if (isPasswordUpdateType(userUpdateType)) {
        if (!isAdmin(profile)) {
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
        controller.subject(subject).update(userUpdateType, request.value).wasAcknowledged()
      }

      if (!wasAcknowledged) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to push the new user's data")
        return@put
      }

      call.respond(HttpStatusCode.OK, "Successfully updated the user's data")
    }
  }
}

private fun isPasswordUpdateType(type: UserUpdateType): Boolean {
  return type == UserUpdateType.Password
}

private fun isAdmin(role: UserProfile): Boolean {
  return role == UserProfile.Admin
}