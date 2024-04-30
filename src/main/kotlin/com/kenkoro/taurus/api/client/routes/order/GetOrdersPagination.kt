package com.kenkoro.taurus.api.client.routes.order

import com.kenkoro.taurus.api.client.controllers.OrderController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.response.order.GetOrders
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.getOrders(
  controller: OrderController,
  config: TokenConfig
) {
  authenticate(config.authName) {
    get("/orders") {
      val page = call.parameters["page"]?.toIntOrNull() ?: 1
      val perPage = call.parameters["per_page"]?.toIntOrNull() ?: 10
      val offset = (page - 1) * perPage
      /*
      val paginatedOrders = controller.readAll(offset, perPage)

      val hasNextPage = paginatedOrders.size >= perPage

      call.respond(
        status = HttpStatusCode.OK,
        message = GetOrders(
          paginatedOrders = paginatedOrders,
          hasNextPage = hasNextPage
        )
      )
       */
    }
  }
}