package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.util.PaginatedOrders
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.getOrders(
  orderController: OrderController,
  config: TokenConfig,
) {
  authenticate(config.authName) {
    get("/orders") {
      val page = call.parameters["page"]?.toIntOrNull() ?: 1
      val perPage = call.parameters["per_page"]?.toIntOrNull() ?: 10
      val offset = (page - 1) * perPage
      val paginatedOrders = orderController.paginatedOrders(offset.toLong(), perPage)

      val hasNextPage = paginatedOrders.size >= perPage

      call.respond(
        status = HttpStatusCode.OK,
        message = PaginatedOrders(paginatedOrders, hasNextPage),
      )
    }
  }
}
