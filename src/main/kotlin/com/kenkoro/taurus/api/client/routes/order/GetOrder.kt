package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.getOrder(
  orderController: OrderController,
  config: TokenConfig,
) {
  authenticate(config.authName) {
    get("/order/{order_id?}") {
      val orderId =
        call.parameters["order_id"]?.toIntOrNull() ?: run {
          call.respond(HttpStatusCode.BadRequest, "The order id is null")
          return@get
        }

      val fetchedOrder = orderController.order(orderId)
      if (fetchedOrder == null) {
        call.respond(HttpStatusCode.NotFound, "Order is not found")
        return@get
      }

      call.respond(
        status = HttpStatusCode.OK,
        message = fetchedOrder,
      )
    }
  }
}
