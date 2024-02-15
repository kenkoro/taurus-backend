package com.kenkoro.taurus.api.client.plugins

import com.kenkoro.taurus.api.client.data.DataSourceType
import com.kenkoro.taurus.api.client.di.ManualDi
import io.ktor.server.application.*

fun Application.configureRouting() {
  val isUnderTest = SutService.get()

  val userRepository = if (!isUnderTest) {
    ManualDi.providePostgresUserRepository(DataSourceType.POSTGRES_TEST)
  } else {
    ManualDi.provideUserRepositoryWithFakeDataSource()
  }

  configureAuthRouting(userRepository)
  configureUserRouting(userRepository)
}