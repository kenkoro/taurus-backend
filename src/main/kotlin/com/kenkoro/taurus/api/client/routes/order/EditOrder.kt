package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.controllers.UserController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.NewOrder
import com.kenkoro.taurus.api.client.models.enums.UserProfile.Admin
import com.kenkoro.taurus.api.client.models.enums.UserProfile.Customer
import com.kenkoro.taurus.api.client.routes.util.Validator
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.editOrder(
  userController: UserController,
  orderController: OrderController,
  config: TokenConfig
) {
  authenticate(config.authName) {
    put("/edit/order") {
      val newOrder = call.receiveNullable<NewOrder>() ?: run {
        call.respond(HttpStatusCode.BadRequest)
        return@put
      }
      val oldOrderId = call.parameters["order_id"]?.toIntOrNull() ?: run {
        call.respond(HttpStatusCode.BadRequest, "You need to specify a concrete order to edit")
        return@put
      }
      val editorSubject = call.parameters["editor_subject"] ?: run {
        call.respond(HttpStatusCode.BadRequest, "The editor's subject must be provided")
        return@put
      }
      val editorProfile = userController.user(editorSubject)?.profile ?: run {
        call.respond(HttpStatusCode.NotFound, "The user who's editing this order is not found")
        return@put
      }

      if (editorProfile != Customer && editorProfile != Admin) {
        call.respond(HttpStatusCode.Conflict, "Only admins and customers are allowed to edit orders")
        return@put
      } else {
        if (!Validator.isNewOrderValid(newOrder)) {
          call.respond(HttpStatusCode.Conflict, "Request has blank data")
          return@put
        }

        val wasAcknowledged = orderController.editOrder(oldOrderId, newOrder)
        if (!wasAcknowledged) {
          call.respond(HttpStatusCode.InternalServerError, "Failed to push the edited order")
          return@put
        }
      }

      call.respond(HttpStatusCode.OK, "Successfully edited the order")
    }
  }
}