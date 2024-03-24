package com.kenkoro.taurus.api.client.core.di

import com.kenkoro.taurus.api.client.services.DbService
import com.kenkoro.taurus.api.client.services.FakeUserCrudService
import com.kenkoro.taurus.api.client.services.PostgresUserCrudService
import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.controllers.UserControllerImpl
import java.sql.Connection
import java.sql.DriverManager

object ManualDi {
  private fun provideDbConnection(): Connection {
    Class.forName("org.postgresql.Driver")
    val (url, user, password) = DbService.credentials()
    return DriverManager.getConnection(url, user, password)
  }

  fun providePostgresUserRepository(): UserController {
    val dbConnection = provideDbConnection()
    val postgresUserCrudService: CrudService = PostgresUserCrudService(dbConnection)

    return UserControllerImpl(postgresUserCrudService)
  }

  fun provideUserRepositoryWithFakeDataSource(): UserController {
    return UserControllerImpl(FakeUserCrudService())
  }
}