package com.kenkoro.taurus.api.client.core.plugins

import com.kenkoro.taurus.api.client.controllers.User
import com.kenkoro.taurus.api.client.routes.auth.login
import com.kenkoro.taurus.api.client.core.security.hashing.HashingService
import com.kenkoro.taurus.api.client.core.security.hashing.SHA256HashingService
import com.kenkoro.taurus.api.client.core.security.token.JwtTokenConfigService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureAuthRouting(user: User) {
  val hashingService: HashingService = SHA256HashingService()
  val config = JwtTokenConfigService.config()

  routing {
    login(user, hashingService, config)
  }
}