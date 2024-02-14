package com.kenkoro.taurus.api.client.plugins

import com.kenkoro.taurus.api.client.data.DataSourceType
import com.kenkoro.taurus.api.client.di.ManualDi
import com.kenkoro.taurus.api.client.route.auth.login
import com.kenkoro.taurus.api.client.security.hashing.HashingService
import com.kenkoro.taurus.api.client.security.hashing.SHA256HashingService
import com.kenkoro.taurus.api.client.security.token.JwtTokenConfigService
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureAuthRouting() {
  val isUnderTest = SutService.get()

  val userRepository = if (!isUnderTest) {
    ManualDi.providePostgresUserRepository(DataSourceType.POSTGRES_TEST)
  } else {
    ManualDi.provideUserRepositoryWithFakeDataSource()
  }
  val hashingService: HashingService = SHA256HashingService()
  val config = JwtTokenConfigService.config()

  routing {
    login(userRepository, hashingService, config)
  }
}