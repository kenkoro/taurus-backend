package com.kenkoro.taurus.api.client.routes.user

import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.hashing.HashingService
import com.kenkoro.taurus.api.client.models.NewUser
import com.kenkoro.taurus.api.client.models.SaltWrapper
import com.kenkoro.taurus.api.client.models.enums.UserProfile.Admin
import com.kenkoro.taurus.api.client.models.setHashedPassword
import com.kenkoro.taurus.api.client.routes.util.Validator
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.createUser(
  controller: UserController,
  hashingService: HashingService,
) {
  post("/add-new/user") {
    val newUser =
      call.receiveNullable<NewUser>() ?: run {
        call.respond(HttpStatusCode.BadRequest)
        return@post
      }
    val creatorSubject =
      call.parameters["creator_subject"] ?: run {
        call.respond(HttpStatusCode.BadRequest, "The creator must be provided")
        return@post
      }
    val creatorProfile =
      controller.user(creatorSubject)?.profile ?: run {
        call.respond(HttpStatusCode.NotFound, "The user who's creating this user is not found")
        return@post
      }

    if (creatorProfile != Admin) {
      call.respond(HttpStatusCode.Conflict, "Only admins are allowed to create new users")
      return@post
    } else {
      if (!Validator.isNewUserValid(newUser)) {
        call.respond(HttpStatusCode.Conflict, "Request has blank data")
        return@post
      }

      val saltedHash = hashingService.hash(newUser.password)
      val newUserWithHashedPassword =
        newUser.setHashedPassword(
          saltedHash.hashedPasswordWithSalt,
        )
      val addedUser =
        controller.addNewUser(
          newUserWithHashedPassword,
          SaltWrapper(saltedHash.salt),
        )

      if (addedUser == null) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to push the new user")
        return@post
      }

      call.respond(
        status = HttpStatusCode.Created,
        message = addedUser,
      )
    }
  }
}
