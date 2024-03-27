package com.kenkoro.taurus.api.client.core.plugins

import com.kenkoro.taurus.api.client.core.di.ManualDi.providePostgresOrderController
import com.kenkoro.taurus.api.client.core.di.ManualDi.providePostgresUserController
import com.kenkoro.taurus.api.client.core.di.ManualDi.provideUserControllerWithFakeUserCrudService
import io.ktor.server.application.*

fun Application.configureRouting() {
  val isUnderTest = SutService.get()

  val userController = if (!isUnderTest) {
    providePostgresUserController()
  } else {
    provideUserControllerWithFakeUserCrudService()
  }
  val orderController = providePostgresOrderController()

  configureAuthRouting(userController)
  configureUserRouting(userController)
  configureOrderRouting(
    userController = userController,
    orderController = orderController
  )
}