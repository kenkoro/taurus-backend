package com.kenkoro.taurus.api.client.core.di

import com.kenkoro.taurus.api.client.services.DbService
import com.kenkoro.taurus.api.client.services.FakeUserService
import com.kenkoro.taurus.api.client.services.PostgresUserService
import com.kenkoro.taurus.api.client.services.UserService
import com.kenkoro.taurus.api.client.controllers.User
import com.kenkoro.taurus.api.client.controllers.UserImpl
import java.sql.Connection
import java.sql.DriverManager

object ManualDi {
  private fun provideDbConnection(): Connection {
    Class.forName("org.postgresql.Driver")
    val (url, user, password) = DbService.credentials()
    return DriverManager.getConnection(url, user, password)
  }

  fun providePostgresUserRepository(): User {
    val dbConnection = provideDbConnection()
    val postgresUserService: UserService = PostgresUserService(dbConnection)

    return UserImpl(postgresUserService)
  }

  fun provideUserRepositoryWithFakeDataSource(): User {
    return UserImpl(FakeUserService())
  }
}