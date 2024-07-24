package com.kenkoro.taurus.api.client.routes.cut.order

import com.kenkoro.taurus.api.client.controllers.CutOrderController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.actualCutOrdersQuantity(
  cutController: CutOrderController,
  config: TokenConfig,
) {
  authenticate(config.authName) {
    get("/order/actual-quantity/{order_id?}") {
      val orderId =
        call.parameters["order_id"]?.toIntOrNull() ?: run {
          call.respond(HttpStatusCode.BadRequest, "The order id is null")
          return@get
        }

      val fetchedActualQuantity = cutController.actualCutOrdersQuantity(orderId)
      if (fetchedActualQuantity == null) {
        call.respond(HttpStatusCode.NotFound, "Cut order is not found")
        return@get
      }

      call.respond(
        status = HttpStatusCode.OK,
        message = mapOf("actual_quantity" to fetchedActualQuantity),
      )
    }
  }
}
