package com.kenkoro.taurus.api.client.core.plugins

import com.kenkoro.taurus.api.client.controllers.User
import com.kenkoro.taurus.api.client.routes.user.createUser
import com.kenkoro.taurus.api.client.routes.user.deleteUser
import com.kenkoro.taurus.api.client.routes.user.getUser
import com.kenkoro.taurus.api.client.routes.user.updateUserData
import com.kenkoro.taurus.api.client.core.security.hashing.HashingService
import com.kenkoro.taurus.api.client.core.security.hashing.SHA256HashingService
import com.kenkoro.taurus.api.client.core.security.token.JwtTokenConfigService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureUserRouting(user: User) {
  val hashingService: HashingService = SHA256HashingService()
  val config = JwtTokenConfigService.config()

  routing {
    createUser(user, hashingService)
    getUser(user, config)
    updateUserData(user, hashingService, config)
    deleteUser(user, config)
  }
}