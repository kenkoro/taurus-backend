package com.kenkoro.taurus.api.client.core.plugins

import com.kenkoro.taurus.api.client.controllers.OrderEditorController
import com.kenkoro.taurus.api.client.core.security.token.JwtTokenConfigService
import com.kenkoro.taurus.api.client.routes.order.editor.isOrderIdUnique
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureOrderEditorRouting(orderEditorController: OrderEditorController) {
  val config = JwtTokenConfigService.config()

  routing {
    isOrderIdUnique(orderEditorController, config)
  }
}
