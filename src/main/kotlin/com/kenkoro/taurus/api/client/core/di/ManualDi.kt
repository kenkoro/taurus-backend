package com.kenkoro.taurus.api.client.core.di

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.controllers.OrderControllerImpl
import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.controllers.UserControllerImpl
import com.kenkoro.taurus.api.client.services.DbService
import com.kenkoro.taurus.api.client.services.crud.user.FakeUserCrudService
import com.kenkoro.taurus.api.client.services.crud.order.PostgresOrderCrudService
import com.kenkoro.taurus.api.client.services.crud.user.PostgresUserCrudService
import java.sql.Connection
import java.sql.DriverManager

object ManualDi {
  private fun provideDbConnection(): Connection {
    Class.forName("org.postgresql.Driver")
    val (url, user, password) = DbService.credentials()
    return DriverManager.getConnection(url.value, user.value, password.value)
  }

  fun providePostgresUserController(): UserController {
    val dbConnection = provideDbConnection()
    val postgresUserCrudService = PostgresUserCrudService(dbConnection)

    return UserControllerImpl(postgresUserCrudService)
  }

  fun providePostgresOrderController(): OrderController {
    return OrderControllerImpl()
  }

  fun provideUserControllerWithFakeUserCrudService(): UserController {
    return UserControllerImpl(
      service = FakeUserCrudService()
    )
  }
}