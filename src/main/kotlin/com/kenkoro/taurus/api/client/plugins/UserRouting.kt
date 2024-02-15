package com.kenkoro.taurus.api.client.plugins

import com.kenkoro.taurus.api.client.data.repository.UserRepository
import com.kenkoro.taurus.api.client.route.user.createUser
import com.kenkoro.taurus.api.client.route.user.deleteUser
import com.kenkoro.taurus.api.client.route.user.getUser
import com.kenkoro.taurus.api.client.route.user.updateUserData
import com.kenkoro.taurus.api.client.security.hashing.HashingService
import com.kenkoro.taurus.api.client.security.hashing.SHA256HashingService
import com.kenkoro.taurus.api.client.security.token.JwtTokenConfigService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureUserRouting(userRepository: UserRepository) {
  val hashingService: HashingService = SHA256HashingService()
  val config = JwtTokenConfigService.config()

  routing {
    createUser(userRepository, hashingService)
    getUser(userRepository, config)
    updateUserData(userRepository, hashingService, config)
    deleteUser(userRepository, config)
  }
}