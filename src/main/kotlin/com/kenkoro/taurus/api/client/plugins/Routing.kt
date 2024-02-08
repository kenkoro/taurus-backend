package com.kenkoro.taurus.api.client.plugins

import com.kenkoro.taurus.api.client.annotation.Warning
import com.kenkoro.taurus.api.client.data.DataSourceType
import com.kenkoro.taurus.api.client.data.remote.PostgresUserDataSource
import com.kenkoro.taurus.api.client.data.remote.UserDataSource
import com.kenkoro.taurus.api.client.data.repository.UserRepository
import com.kenkoro.taurus.api.client.data.repository.UserRepositoryImpl
import com.kenkoro.taurus.api.client.di.ManualDi.provideDb
import com.kenkoro.taurus.api.client.route.auth.login
import com.kenkoro.taurus.api.client.route.user.createUser
import com.kenkoro.taurus.api.client.route.user.getUserByItsSubject
import com.kenkoro.taurus.api.client.route.user.updateUserSubject
import com.kenkoro.taurus.api.client.security.hashing.HashingService
import com.kenkoro.taurus.api.client.security.hashing.SHA256HashingService
import com.kenkoro.taurus.api.client.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting(config: TokenConfig) {
  val db = provideDb(DataSourceType.POSTGRES_TEST)
  val postgresUserDataSource: UserDataSource = PostgresUserDataSource(db)
  val userRepository: UserRepository = UserRepositoryImpl(postgresUserDataSource)
  val hashingService: HashingService = SHA256HashingService()

  configureAuthRouting(config = config, userRepository = userRepository, hashingService = hashingService)
  configureUserRouting(config = config, userRepository = userRepository, hashingService = hashingService)
}

@Warning("Encapsulate it to a separate file")
private fun Application.configureAuthRouting(
  config: TokenConfig,
  userRepository: UserRepository,
  hashingService: HashingService
) {
  routing {
    login(userRepository, hashingService, config)
  }
}

@Warning("Encapsulate it to a separate file")
private fun Application.configureUserRouting(
  config: TokenConfig,
  userRepository: UserRepository,
  hashingService: HashingService
) {
  routing {
    createUser(userRepository, hashingService)
    getUserByItsSubject(userRepository, config)
    updateUserSubject(userRepository, config)
  }
}