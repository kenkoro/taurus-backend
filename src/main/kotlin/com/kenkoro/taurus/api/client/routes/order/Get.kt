package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getOrder(
  controller: OrderController,
  config: TokenConfig
) {
  authenticate(config.authName) {
    get("/order/{order_id?}") {
      val orderId = call.parameters["order_id"]?.toIntOrNull() ?: run {
        call.respond(HttpStatusCode.BadRequest, "The order id is null")
        return@get
      }

      /*
      val fetchedOrder = controller
        .orderId(orderId)
        .read()

      call.respond(
        status = HttpStatusCode.OK,
        message = fetchedOrder
      )
       */
    }
  }
}