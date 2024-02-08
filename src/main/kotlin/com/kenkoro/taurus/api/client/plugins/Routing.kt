package com.kenkoro.taurus.api.client.plugins

import com.kenkoro.taurus.api.client.data.DataSourceType
import com.kenkoro.taurus.api.client.data.UserDataSource
import com.kenkoro.taurus.api.client.data.remote.PostgresUserDataSource
import com.kenkoro.taurus.api.client.data.repository.UserRepository
import com.kenkoro.taurus.api.client.data.repository.UserRepositoryImpl
import com.kenkoro.taurus.api.client.di.ManualDi.provideDb
import com.kenkoro.taurus.api.client.route.auth.login
import com.kenkoro.taurus.api.client.route.auth.createUser
import com.kenkoro.taurus.api.client.security.hashing.HashingService
import com.kenkoro.taurus.api.client.security.hashing.SHA256HashingService
import com.kenkoro.taurus.api.client.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureAuthRouting(config: TokenConfig) {
  routing {
    val db = provideDb(DataSourceType.POSTGRES_TEST)
    val postgresUserDataSource: UserDataSource = PostgresUserDataSource(db)
    val userRepository: UserRepository = UserRepositoryImpl(postgresUserDataSource)
    val hashingService: HashingService = SHA256HashingService()

    createUser(userRepository, hashingService)
    login(userRepository, hashingService, config)
  }
}