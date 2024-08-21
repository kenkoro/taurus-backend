package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.EditOrder
import com.kenkoro.taurus.api.client.models.enums.OrderStatus
import com.kenkoro.taurus.api.client.models.enums.UserProfile
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.put

fun Route.toCutOrder(
  userController: UserController,
  orderController: OrderController,
  config: TokenConfig,
) {
  authenticate(config.authName) {
    put("/to-cut-order/{order_id?}") {
      val orderId =
        call.parameters["order_id"]?.toIntOrNull() ?: run {
          call.respond(HttpStatusCode.BadRequest, "The order id is null")
          return@put
        }
      val cutterSubject =
        call.parameters["cutter_subject"] ?: run {
          call.respond(HttpStatusCode.BadRequest, "The cutter's subject must be provided")
          return@put
        }
      val cutter =
        userController.user(cutterSubject) ?: run {
          call.respond(HttpStatusCode.NotFound, "The user who's editing this order is not found")
          return@put
        }

      if (cutter.profile != UserProfile.Cutter) {
        call.respond(HttpStatusCode.Conflict, "Only cutters are allowed to cut orders")
        return@put
      } else {
        val orderToEdit = orderController.order(orderId)
        if (orderToEdit == null) {
          call.respond(HttpStatusCode.NotFound, "The order with this id is not found")
          return@put
        }
        val wasAcknowledged =
          orderController.editOrder(
            EditOrder(
              orderId = orderToEdit.orderId,
              customer = orderToEdit.customer,
              title = orderToEdit.title,
              model = orderToEdit.model,
              size = orderToEdit.size,
              color = orderToEdit.color,
              category = orderToEdit.category,
              quantity = orderToEdit.quantity,
              status = OrderStatus.Cut,
              creatorId = orderToEdit.creatorId,
            ),
          )
        if (!wasAcknowledged) {
          call.respond(HttpStatusCode.InternalServerError, "Failed to cut the order")
          return@put
        }
      }

      call.respond(HttpStatusCode.OK, "Successfully cut the order")
    }
  }
}
