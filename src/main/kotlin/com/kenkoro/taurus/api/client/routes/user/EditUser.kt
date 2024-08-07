package com.kenkoro.taurus.api.client.routes.user

import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.hashing.HashingService
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.NewUser
import com.kenkoro.taurus.api.client.models.enums.UserProfile.Admin
import com.kenkoro.taurus.api.client.models.setHashedPassword
import com.kenkoro.taurus.api.client.routes.util.Validator
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.put

fun Route.editUser(
  userController: UserController,
  hashingService: HashingService,
  config: TokenConfig,
) {
  authenticate(config.authName) {
    put("/edit/user") {
      val newUser =
        call.receiveNullable<NewUser>() ?: run {
          call.respond(HttpStatusCode.BadRequest)
          return@put
        }
      val oldUserSubject =
        call.parameters["subject"] ?: run {
          call.respond(HttpStatusCode.BadRequest, "You need to specify a concrete user to edit")
          return@put
        }
      val editorSubject =
        call.parameters["editor_subject"] ?: run {
          call.respond(HttpStatusCode.BadRequest, "The editor's subject must be provided")
          return@put
        }
      val editorProfile =
        userController.user(editorSubject)?.profile ?: run {
          call.respond(HttpStatusCode.NotFound, "The user who's editing this user is not found")
          return@put
        }

      if (editorProfile != Admin) {
        call.respond(HttpStatusCode.Conflict, "Only admins are allowed to edit users")
      } else {
        val saltedHash = hashingService.hash(newUser.password)
        val newUserWithHashedPassword =
          newUser.setHashedPassword(
            saltedHash.hashedPasswordWithSalt,
          )
        if (!Validator.isNewUserValid(newUserWithHashedPassword)) {
          call.respond(HttpStatusCode.Conflict, "Request has blank data")
          return@put
        }

        val wasAcknowledged = userController.editUser(oldUserSubject, newUserWithHashedPassword)
        if (!wasAcknowledged) {
          call.respond(
            HttpStatusCode.InternalServerError,
            "Failed to push the edited user",
          )
          return@put
        }
      }

      call.respond(HttpStatusCode.OK, "Successfully edited the user")
    }
  }
}
