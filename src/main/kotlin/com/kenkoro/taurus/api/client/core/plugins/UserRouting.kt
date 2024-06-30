package com.kenkoro.taurus.api.client.core.plugins

import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.hashing.HashingService
import com.kenkoro.taurus.api.client.core.security.hashing.SHA256HashingService
import com.kenkoro.taurus.api.client.core.security.token.JwtTokenConfigService
import com.kenkoro.taurus.api.client.routes.user.createUser
import com.kenkoro.taurus.api.client.routes.user.deleteUser
import com.kenkoro.taurus.api.client.routes.user.editUser
import com.kenkoro.taurus.api.client.routes.user.getUser
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureUserRouting(userController: UserController) {
  val hashingService: HashingService = SHA256HashingService()
  val config = JwtTokenConfigService.config()

  routing {
    createUser(userController, hashingService)
    getUser(userController, config)
    editUser(userController, hashingService, config)
    deleteUser(userController, config)
  }
}
