package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.NewOrder
import com.kenkoro.taurus.api.client.routes.util.Validator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.addNewOrder(
  controller: OrderController,
  config: TokenConfig
) {
  authenticate(config.authName) {
    post("/add-new/order") {
      val newOrder = call.receiveNullable<NewOrder>() ?: run {
        call.respond(HttpStatusCode.BadRequest)
        return@post
      }

      if (!Validator.isNewOrderValid(newOrder)) {
        call.respond(HttpStatusCode.Conflict, "Request has blank data")
        return@post
      }

      val addedOrder = controller.addNewOrder(newOrder)
      if (addedOrder == null) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to push the new order")
        return@post
      }

      call.respond(
        status = HttpStatusCode.Created,
        message = addedOrder
      )
    }
  }
}