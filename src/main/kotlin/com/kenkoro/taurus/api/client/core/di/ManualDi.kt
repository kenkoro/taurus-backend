package com.kenkoro.taurus.api.client.core.di

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.controllers.OrderControllerImpl
import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.controllers.UserControllerImpl
import com.kenkoro.taurus.api.client.services.DbService
import com.kenkoro.taurus.api.client.services.FakeUserCrudService
import com.kenkoro.taurus.api.client.services.PostgresOrderCrudService
import com.kenkoro.taurus.api.client.services.PostgresUserCrudService
import java.sql.Connection
import java.sql.DriverManager

object ManualDi {
  private fun provideDbConnection(): Connection {
    Class.forName("org.postgresql.Driver")
    val (url, user, password) = DbService.credentials()
    return DriverManager.getConnection(url, user, password)
  }

  fun providePostgresUserController(): UserController {
    val dbConnection = provideDbConnection()
    val postgresUserCrudService = PostgresUserCrudService(dbConnection)

    return UserControllerImpl(postgresUserCrudService)
  }

  fun providePostgresOrderController(): OrderController {
    val dbConnection = provideDbConnection()
    val postgresOrderCrudService = PostgresOrderCrudService(dbConnection)

    return OrderControllerImpl(postgresOrderCrudService)
  }

  fun provideUserControllerWithFakeUserCrudService(): UserController {
    return UserControllerImpl(
      service = FakeUserCrudService()
    )
  }
}