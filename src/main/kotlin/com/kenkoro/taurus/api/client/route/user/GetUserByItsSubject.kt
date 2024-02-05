package com.kenkoro.taurus.api.client.route.user

import com.kenkoro.taurus.api.client.data.repository.UserRepository
import io.ktor.server.routing.*

fun Route.getUserByItsSubject(
  userRepository: UserRepository
) {
  get("/api/user/{id?}") {
    TODO("Implement the getUserByItsSubject route")
  }
}