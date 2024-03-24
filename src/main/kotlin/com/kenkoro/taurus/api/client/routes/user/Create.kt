package com.kenkoro.taurus.api.client.routes.user

import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.mappers.fromRequest
import com.kenkoro.taurus.api.client.core.security.hashing.HashingService
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

    if (!isReceivedDataValid(request)) {
      call.respond(HttpStatusCode.Conflict, "Request has blank data")
      return@post
    }

    val saltedHash = hashingService.hash(request.password)
    val model = CreateUserWithSalt.fromRequest(request, saltedHash)
    val wasAcknowledged = controller.model(model).create()

    if (!wasAcknowledged) {
      call.respond(HttpStatusCode.InternalServerError, "Failed to push the new user")
      return@post
    }

    call.respond(
      status = HttpStatusCode.Created,
      message = "Successfully created the new user"
    )
  }
}

private fun isReceivedDataValid(request: CreateUser): Boolean {
  return request.subject.isNotBlank()
      && request.password.isNotBlank()
      && request.firstName.isNotBlank()
      && request.profile.name.isNotBlank()
}