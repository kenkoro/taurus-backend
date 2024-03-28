package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.request.order.DeleteOrder
import com.kenkoro.taurus.api.client.models.util.UserProfile
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.deleteOrder(
  userController: UserController,
  orderController: OrderController,
  config: TokenConfig
) {
  authenticate(config.authName) {
    delete("/delete/order/{order_id?}") {
      val deleter = call.receiveNullable<DeleteOrder>()?.deleter ?: run {
        call.respond(HttpStatusCode.BadRequest)
        return@delete
      }
      val orderId = call.parameters["order_id"]?.toIntOrNull() ?: run {
        call.respond(HttpStatusCode.BadRequest, "The order id is null")
        return@delete
      }

      val deleterProfile = userController
        .subject(deleter)
        .read().profile
      if (deleterProfile != UserProfile.Customer) {
        call.respond(HttpStatusCode.Conflict, "Only customers can delete the orders")
        return@delete
      }

      val wasAcknowledged = orderController
        .orderId(orderId)
        .delete()
        .wasAcknowledged()
      if (!wasAcknowledged) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to delete the order")
        return@delete
      }

      call.respond(HttpStatusCode.OK, "Successfully deleted the order")
    }
  }
}