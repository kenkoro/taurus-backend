package com.kenkoro.taurus.api.client.core.plugins

import com.kenkoro.taurus.api.client.controllers.CutOrderControllerImpl
import com.kenkoro.taurus.api.client.controllers.OrderControllerImpl
import com.kenkoro.taurus.api.client.controllers.UserControllerImpl
import io.ktor.server.application.Application

fun Application.configureRouting() {
  // For testing purposes
  val isUnderTest = SutService.get()

  val userController = UserControllerImpl()
  val orderController = OrderControllerImpl()
  val cutOrderController = CutOrderControllerImpl()

  configureAuthRouting(userController)
  configureUserRouting(userController)
  configureOrderRouting(userController, orderController, cutOrderController)
}
