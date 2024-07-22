package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.CutOrderController
import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.NewCutOrder
import com.kenkoro.taurus.api.client.models.NewOrder
import com.kenkoro.taurus.api.client.models.enums.OrderStatus
import com.kenkoro.taurus.api.client.models.enums.UserProfile
import com.kenkoro.taurus.api.client.models.enums.UserProfile.Admin
import com.kenkoro.taurus.api.client.models.enums.UserProfile.Customer
import com.kenkoro.taurus.api.client.models.enums.UserProfile.Cutter
import com.kenkoro.taurus.api.client.models.enums.UserProfile.Inspector
import com.kenkoro.taurus.api.client.routes.util.Validator
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.put

fun Route.editOrder(
  userController: UserController,
  orderController: OrderController,
  cutOrderController: CutOrderController,
  config: TokenConfig,
) {
  authenticate(config.authName) {
    put("/edit/order") {
      val newOrder =
        call.receiveNullable<NewOrder>() ?: run {
          call.respond(HttpStatusCode.BadRequest)
          return@put
        }
      val orderId =
        call.parameters["order_id"]?.toIntOrNull() ?: run {
          call.respond(HttpStatusCode.BadRequest, "You need to specify a concrete order to edit")
          return@put
        }
      val editorSubject =
        call.parameters["editor_subject"] ?: run {
          call.respond(HttpStatusCode.BadRequest, "The editor's subject must be provided")
          return@put
        }
      val editor =
        userController.user(editorSubject) ?: run {
          call.respond(HttpStatusCode.NotFound, "The user who's editing this order is not found")
          return@put
        }
      val oldOrderStatus =
        orderController.order(orderId)?.status ?: run {
          call.respond(HttpStatusCode.NotFound, "The order status not found")
          return@put
        }

      if (!isAllowedToEdit(editor.profile)) {
        call.respond(
          HttpStatusCode.Conflict,
          "Only admins, customers, cutters, and inspectors are allowed to edit orders",
        )
        return@put
      } else {
        if (!Validator.isNewOrderValid(newOrder)) {
          call.respond(HttpStatusCode.Conflict, "Request has blank data")
          return@put
        }

        if (
          seeIfOrderStatusChangedToCut(newOrder.status, oldOrderStatus) &&
          editor.profile == Cutter
        ) {
          val addedCutOrder =
            cutOrderController.addNewCutOrder(
              NewCutOrder(
                orderId = orderId,
                date = 0L,
                quantity = newOrder.quantity,
                cutterId = editor.userId,
                comment = "",
              ),
            )

          if (addedCutOrder == null) {
            call.respond(HttpStatusCode.Conflict, "Can't add new cut order")
            return@put
          }
        }

        val wasAcknowledged = orderController.editOrder(orderId, newOrder)
        if (!wasAcknowledged) {
          call.respond(
            HttpStatusCode.InternalServerError,
            "Failed to push the edited order",
          )
          return@put
        }
      }

      call.respond(HttpStatusCode.OK, "Successfully edited the order")
    }
  }
}

private fun isAllowedToEdit(userProfile: UserProfile): Boolean {
  return userProfile == Customer ||
    userProfile == Admin ||
    userProfile == Cutter ||
    userProfile == Inspector
}

private fun seeIfOrderStatusChangedToCut(
  newStatus: OrderStatus,
  oldStatus: OrderStatus,
): Boolean {
  return oldStatus == OrderStatus.Idle && newStatus == OrderStatus.Cut
}
