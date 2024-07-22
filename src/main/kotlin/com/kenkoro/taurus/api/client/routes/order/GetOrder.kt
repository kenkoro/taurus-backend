package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.CutOrderController
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
  cutOrderController: CutOrderController,
  config: TokenConfig,
) {
  authenticate(config.authName) {
    get("/order/{order_id?}") {
      val orderId =
        call.parameters["order_id"]?.toIntOrNull() ?: run {
          call.respond(HttpStatusCode.BadRequest, "The order id is null")
          return@get
        }
      val actualCutOrdersQuantity = call.parameters["actual_quantity"]?.toBooleanStrictOrNull()

      if (actualCutOrdersQuantity != null) {
        val actualQuantity = cutOrderController.actualCutOrdersQuantity(orderId)
        if (actualQuantity == null) {
          call.respond(HttpStatusCode.NotFound, "Cut order is not found")
          return@get
        }

        call.respond(
          status = HttpStatusCode.OK,
          message = mapOf("actual_quantity" to actualQuantity),
        )
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
