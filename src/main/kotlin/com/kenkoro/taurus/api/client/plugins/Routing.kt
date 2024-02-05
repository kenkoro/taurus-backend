package com.kenkoro.taurus.api.client.plugins

import com.kenkoro.taurus.api.client.data.DataSourceType
import com.kenkoro.taurus.api.client.data.UserDataSource
import com.kenkoro.taurus.api.client.data.remote.PostgresUserDataSource
import com.kenkoro.taurus.api.client.data.repository.UserRepository
import com.kenkoro.taurus.api.client.data.repository.UserRepositoryImpl
import com.kenkoro.taurus.api.client.di.ManualDi.provideDb
import com.kenkoro.taurus.api.client.route.auth.signIn
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureAuthRouting() {
  routing {
    val db = provideDb(DataSourceType.POSTGRES_TEST)
    val postgresUserDataSource: UserDataSource = PostgresUserDataSource(db)
    val userRepository: UserRepository = UserRepositoryImpl(postgresUserDataSource)

    signIn(userRepository)
  }
}
