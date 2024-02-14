package com.kenkoro.taurus.api.client.plugins

import com.kenkoro.taurus.api.client.data.DataSourceType
import com.kenkoro.taurus.api.client.di.ManualDi
import com.kenkoro.taurus.api.client.route.user.createUser
import com.kenkoro.taurus.api.client.route.user.deleteUser
import com.kenkoro.taurus.api.client.route.user.getUserByItsSubject
import com.kenkoro.taurus.api.client.route.user.updateUserData
import com.kenkoro.taurus.api.client.security.hashing.HashingService
import com.kenkoro.taurus.api.client.security.hashing.SHA256HashingService
import com.kenkoro.taurus.api.client.security.token.JwtTokenConfigService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureUserRouting() {
  val isUnderTest = SutService.get()

  val userRepository = if (!isUnderTest) {
    ManualDi.providePostgresUserRepository(DataSourceType.POSTGRES_TEST)
  } else {
    ManualDi.provideUserRepositoryWithFakeDataSource()
  }
  val hashingService: HashingService = SHA256HashingService()
  val config = JwtTokenConfigService.config()

  routing {
    createUser(userRepository, hashingService)
    getUserByItsSubject(userRepository, config)
    updateUserData(userRepository, hashingService, config)
    deleteUser(userRepository, config)
  }
}