package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.NewOrder
import com.kenkoro.taurus.api.client.routes.util.Validator
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.addNewOrder(
  orderController: OrderController,
  config: TokenConfig,
) {
  authenticate(config.authName) {
    post("/add-new/order") {
      val newOrder =
        call.receiveNullable<NewOrder>() ?: run {
          call.respond(HttpStatusCode.BadRequest)
          return@post
        }

      if (!Validator.isNewOrderValid(newOrder)) {
        call.respond(HttpStatusCode.Conflict, "Request has blank data")
        return@post
      }

      val addedOrder = orderController.addNewOrder(newOrder)
      if (addedOrder == null) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to push the new order")
        return@post
      }

      call.respond(
        status = HttpStatusCode.Created,
        message = addedOrder,
      )
    }
  }
}
