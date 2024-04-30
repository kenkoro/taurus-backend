package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.enums.UserProfile
import com.kenkoro.taurus.api.client.routes.util.RouteService
import com.kenkoro.taurus.api.client.services.util.OrderUpdateType
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.updateOrderData(
  userController: UserController,
  orderController: OrderController,
  config: TokenConfig
) {
  authenticate(config.authName) {
    put("/order/{order_id?}/edit/{data?}") {
      val orderId = call.parameters["order_id"]?.toIntOrNull() ?: run {
        call.respond(HttpStatusCode.BadRequest, "The order id is null")
        return@put
      }
      val (request, data) = RouteService.handleDataParameterAndGetItWithRequest(call) ?: run {
        call.respond(HttpStatusCode.Conflict)
        return@put
      }
      val updaterProfile = userController.subject(request.updater).read().profile

      val orderUpdateType = try {
        OrderUpdateType.valueOf(data.replaceFirstChar { it.uppercase() })
      } catch (iae: IllegalArgumentException) {
        call.respond(HttpStatusCode.BadRequest, "Not a valid order's data to update")
        return@put
      }

      /*
      val wasAcknowledged = if (isCustomer(updaterProfile)) {
        orderController
          .orderId(orderId)
          .update(orderUpdateType, request.value)
          .wasAcknowledged()
      } else {
        call
          .respond(HttpStatusCode.Conflict, "Only users with customer profile can change the orders")
        return@put
      }

      if (!wasAcknowledged) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to push the new order's data")
        return@put
      }

      call.respond(HttpStatusCode.OK, "Successfully updated the order's data")
       */
    }
  }
}

private fun isCustomer(profile: UserProfile): Boolean {
  return profile == UserProfile.Customer
}