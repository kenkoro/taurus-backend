package com.kenkoro.taurus.api.client.core.plugins

import com.kenkoro.taurus.api.client.controllers.CutOrderController
import com.kenkoro.taurus.api.client.core.security.token.JwtTokenConfigService
import com.kenkoro.taurus.api.client.routes.cut.order.actualCutOrdersQuantity
import com.kenkoro.taurus.api.client.routes.cut.order.addNewCutOrder
import io.ktor.server.application.Application
import io.ktor.server.routing.routing

fun Application.configureCutOrderRouting(cutOrderController: CutOrderController) {
  val config = JwtTokenConfigService.config()

  routing {
    addNewCutOrder(cutOrderController, config)
    actualCutOrdersQuantity(cutOrderController, config)
  }
}
