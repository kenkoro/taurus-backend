package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.request.order.Order
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.createOrder(
  controller: OrderController,
  config: TokenConfig
) {
  authenticate(config.authName) {
    post("/new/order") {
      val request = call.receiveNullable<Order>() ?: run {
        call.respond(HttpStatusCode.BadRequest)
        return@post
      }

      if (!isReceivedDataValid(request)) {
        call.respond(HttpStatusCode.Conflict, "Request has blank data")
        return@post
      }

      val wasAcknowledged = controller.model(request).create()

      if (!wasAcknowledged) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to push the new order")
        return@post
      }

      call.respond(
        status = HttpStatusCode.Created,
        message = "Successfully created the new order"
      )
    }
  }
}

private fun isReceivedDataValid(request: Order): Boolean {
  return request.customer.isNotBlank()
      && request.date.isNotBlank()
      && request.title.isNotBlank()
      && request.model.isNotBlank()
      && request.size.isNotBlank()
      && request.color.isNotBlank()
      && request.category.isNotBlank()
      && request.quantity > 0
}