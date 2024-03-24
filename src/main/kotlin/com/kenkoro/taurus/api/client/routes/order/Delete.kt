package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteOrder(
  controller: OrderController,
  config: TokenConfig
) {
  authenticate(config.authName) {
    delete("/delete/order/{orderId?}") {
      val orderId = call.parameters["orderId"]?.toIntOrNull() ?: run {
        return@delete call.respond(HttpStatusCode.BadRequest)
      }

      // TODO: Business logic
    }
  }
}