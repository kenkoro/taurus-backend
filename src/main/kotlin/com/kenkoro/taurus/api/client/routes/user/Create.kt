package com.kenkoro.taurus.api.client.routes.user

import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.hashing.HashingService
import com.kenkoro.taurus.api.client.core.security.hashing.SaltedHash
import com.kenkoro.taurus.api.client.models.request.user.CreateUser
import com.kenkoro.taurus.api.client.models.request.user.CreateUserWithSalt
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createUser(
  controller: UserController,
  hashingService: HashingService
) {
  post("/new/user") {
    val request = call.receiveNullable<CreateUser>() ?: run {
      call.respond(HttpStatusCode.BadRequest)
      return@post
    }

    if (!isCredentialsValid(request)) {
      call.respond(HttpStatusCode.Conflict, "Credentials are not valid")
      return@post
    }

    val saltedHash = hashingService.hash(request.password)
    val model = createUserModelWithSalt(request, saltedHash)
    val wasAcknowledged = controller.model(model).create()

    if (!wasAcknowledged) {
      call.respond(HttpStatusCode.InternalServerError, "Failed to push the new user")
      return@post
    }

    call.respond(HttpStatusCode.Created, "Successfully created the new user")
  }
}

private fun isCredentialsValid(request: CreateUser): Boolean {
  return request.subject.isNotBlank()
      && request.password.isNotBlank()
      && request.firstName.isNotBlank()
      && request.profile.name.isNotBlank()
}

private fun createUserModelWithSalt(request: CreateUser, saltedHash: SaltedHash): CreateUserWithSalt {
  return CreateUserWithSalt(
    subject = request.subject,
    password = saltedHash.hashedPasswordWithSalt,
    image = request.image,
    firstName = request.firstName,
    lastName = request.lastName,
    email = request.email,
    profile = request.profile,
    salt = saltedHash.salt
  )
}