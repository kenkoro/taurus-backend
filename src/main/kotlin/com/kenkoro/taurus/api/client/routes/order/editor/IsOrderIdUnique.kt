package com.kenkoro.taurus.api.client.routes.order.editor

import com.kenkoro.taurus.api.client.controllers.OrderEditorController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import io.ktor.server.auth.authenticate
import io.ktor.server.routing.Route
import io.ktor.server.routing.get

fun Route.isOrderIdUnique(
  controller: OrderEditorController,
  config: TokenConfig,
) {
  authenticate(config.authName) {
    get("/is-order-unique/{order_id?}") {
      // TODO: Left out, remove it
    }
  }
}
