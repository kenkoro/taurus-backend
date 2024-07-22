package com.kenkoro.taurus.api.client.core.plugins

import com.kenkoro.taurus.api.client.controllers.CutOrderController
import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.token.JwtTokenConfigService
import com.kenkoro.taurus.api.client.routes.order.addNewOrder
import com.kenkoro.taurus.api.client.routes.order.deleteOrder
import com.kenkoro.taurus.api.client.routes.order.editOrder
import com.kenkoro.taurus.api.client.routes.order.getOrder
import com.kenkoro.taurus.api.client.routes.order.getOrders
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureOrderRouting(
  userController: UserController,
  orderController: OrderController,
  cutOrderController: CutOrderController,
) {
  val config = JwtTokenConfigService.config()

  routing {
    addNewOrder(orderController, config)
    deleteOrder(userController, orderController, config)
    getOrder(orderController, cutOrderController, config)
    getOrders(orderController, config)
    editOrder(userController, orderController, cutOrderController, config)
  }
}
