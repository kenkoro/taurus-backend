package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.dto.DeleteDto
import com.kenkoro.taurus.api.client.models.enums.UserProfile.Admin
import com.kenkoro.taurus.api.client.models.enums.UserProfile.Customer
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.delete

fun Route.deleteOrder(
  userController: UserController,
  orderController: OrderController,
  config: TokenConfig,
) {
  authenticate(config.authName) {
    delete("/delete/order/{order_id?}") {
      val deleterSubject =
        call.receiveNullable<DeleteDto>()?.deleterSubject ?: run {
          call.respond(HttpStatusCode.BadRequest)
          return@delete
        }
      val orderId =
        call.parameters["order_id"]?.toIntOrNull() ?: run {
          call.respond(HttpStatusCode.BadRequest, "The order id is null")
          return@delete
        }
      val deleterProfile =
        userController.user(deleterSubject)?.profile ?: run {
          call.respond(HttpStatusCode.NotFound, "The user who's deleting this order is not found")
          return@delete
        }
      if (deleterProfile != Customer && deleterProfile != Admin) {
        call.respond(HttpStatusCode.Conflict, "Only customers and admins can delete orders")
        return@delete
      }

      val wasAcknowledged = orderController.deleteOrder(orderId)
      if (!wasAcknowledged) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to delete the order")
        return@delete
      }

      call.respond(HttpStatusCode.OK, "Successfully deleted the order")
    }
  }
}
