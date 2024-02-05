package com.kenkoro.taurus.api.client.route.auth

import com.kenkoro.taurus.api.client.data.repository.UserRepository
import com.kenkoro.taurus.api.client.model.request.SignInRequest
import com.kenkoro.taurus.api.client.model.response.AuthResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.signIn(
  userRepository: UserRepository
) {
  post("/api/signIn") {
    val request = call.receiveNullable<SignInRequest>() ?: run {
      call.respond(HttpStatusCode.BadRequest)
      return@post
    }

    /**
     * TODO:
     * 1. Get a user from a DB
     * 2. Check the password w/ salt
     * 3. Regenerate the token
     */

    call.respond(
      status = HttpStatusCode.OK,
      message = AuthResponse(
        token = ""
      )
    )
  }
}