package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.orm.NewOrder
import com.kenkoro.taurus.api.client.services.dao.OrderFields
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun Route.addNewOrder(
  controller: OrderController,
  config: TokenConfig
) {
  post("/add-new/order") {
    val request = call.receiveNullable<NewOrder>() ?: run {
      call.respond(HttpStatusCode.BadRequest)
      return@post
    }

    if (!isReceivedDataValid(request)) {
      call.respond(HttpStatusCode.Conflict, "Request has blank data")
      return@post
    }

    val addedOrder = controller.addNewOrder(
      request.orderId, OrderFields(
        customer = request.customer,
        title = request.title,
        model = request.model,
        size = request.size,
        color = request.color,
        category = request.category,
        quantity = request.quantity,
      )
    )

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

private fun isReceivedDataValid(request: NewOrder): Boolean {
  return request.customer.isNotBlank()
      && request.title.isNotBlank()
      && request.model.isNotBlank()
      && request.size.isNotBlank()
      && request.color.isNotBlank()
      && request.category.isNotBlank()
      && request.quantity > 0
}