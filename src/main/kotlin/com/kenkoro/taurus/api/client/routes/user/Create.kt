package com.kenkoro.taurus.api.client.routes.user

import com.kenkoro.taurus.api.client.controllers.User
import com.kenkoro.taurus.api.client.core.security.hashing.HashingService
import com.kenkoro.taurus.api.client.core.security.hashing.SaltedHash
import com.kenkoro.taurus.api.client.models.request.user.Create
import com.kenkoro.taurus.api.client.models.request.user.CreateWithSalt
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createUser(
  user: User,
  hashingService: HashingService
) {
  post("/api/create/user") {
    val request = call.receiveNullable<Create>() ?: run {
      call.respond(HttpStatusCode.BadRequest)
      return@post
    }

    if (!isCredentialsValid(request)) {
      call.respond(HttpStatusCode.Conflict, "Credentials are not valid")
      return@post
    }

    val saltedHash = hashingService.hash(request.password)
    val model = createUser(request, saltedHash)
    val wasAcknowledged = user.user(model).create()

    if (!wasAcknowledged) {
      call.respond(HttpStatusCode.InternalServerError, "Failed to push the new user")
      return@post
    }

    call.respond(HttpStatusCode.Created, "Successfully created the new user")
  }
}

private fun isCredentialsValid(request: Create): Boolean {
  return request.subject.isNotBlank()
      && request.password.isNotBlank()
      && request.firstName.isNotBlank()
      && request.role.name.isNotBlank()
}

private fun createUser(request: Create, saltedHash: SaltedHash): CreateWithSalt {
  return CreateWithSalt(
    subject = request.subject,
    password = saltedHash.hashedPasswordWithSalt,
    image = request.image,
    firstName = request.firstName,
    lastName = request.lastName,
    role = request.role,
    salt = saltedHash.salt
  )
}