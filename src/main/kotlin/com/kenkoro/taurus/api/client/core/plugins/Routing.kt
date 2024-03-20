package com.kenkoro.taurus.api.client.core.plugins

import com.kenkoro.taurus.api.client.core.di.ManualDi
import io.ktor.server.application.*

fun Application.configureRouting() {
  val isUnderTest = SutService.get()

  val userRepository = if (!isUnderTest) {
    ManualDi.providePostgresUserRepository()
  } else {
    ManualDi.provideUserRepositoryWithFakeDataSource()
  }

  configureAuthRouting(userRepository)
  configureUserRouting(userRepository)
}