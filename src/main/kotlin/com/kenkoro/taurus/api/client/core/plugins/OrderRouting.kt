package com.kenkoro.taurus.api.client.core.plugins

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.token.JwtTokenConfigService
import com.kenkoro.taurus.api.client.routes.order.*
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureOrderRouting(orderController: OrderController) {
  val config = JwtTokenConfigService.config()

  routing {
    addNewOrder(orderController, config)
  }
}