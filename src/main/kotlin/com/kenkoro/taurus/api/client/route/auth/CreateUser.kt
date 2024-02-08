package com.kenkoro.taurus.api.client.route.auth

import com.kenkoro.taurus.api.client.data.repository.UserRepository
import com.kenkoro.taurus.api.client.model.InsertingUserModel
import com.kenkoro.taurus.api.client.model.request.SignUpRequest
import com.kenkoro.taurus.api.client.security.hashing.HashingService
import com.kenkoro.taurus.api.client.security.hashing.SaltedHash
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createUser(
  userRepository: UserRepository,
  hashingService: HashingService
) {
  post("/api/create/user") {
    val request = call.receiveNullable<SignUpRequest>() ?: run {
      call.respond(HttpStatusCode.BadRequest)
      return@post
    }

    if (!isCredentialsValid(request)) {
      call.respond(HttpStatusCode.UnprocessableEntity, "Credentials are not valid")
      return@post
    }

    val saltedHash = hashingService.hash(request.password)
    val user = createUser(request, saltedHash)
    val wasAcknowledged = userRepository.createUser(user)

    if (!wasAcknowledged) {
      call.respond(HttpStatusCode.InternalServerError, "Failed to push a new user")
      return@post
    }

    call.respond(HttpStatusCode.Created, "Sign up was successful")
  }
}

private fun isCredentialsValid(request: SignUpRequest): Boolean {
  return request.subject.isNotBlank()
      && request.password.isNotBlank()
      && request.firstName.isNotBlank()
      && request.role.name.isNotBlank()
}

private fun createUser(request: SignUpRequest, saltedHash: SaltedHash): InsertingUserModel {
  return InsertingUserModel(
    subject = request.subject,
    password = saltedHash.hashedPasswordWithSalt,
    image = request.image,
    firstName = request.firstName,
    lastName = request.lastName,
    role = request.role,
    salt = saltedHash.salt
  )
}