package com.kenkoro.taurus.api.client.core.plugins

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.controllers.OrderControllerImpl
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
  val orderController = OrderControllerImpl()

  configureAuthRouting(userController)
  configureUserRouting(userController)
  configureOrderRouting(orderController)
}