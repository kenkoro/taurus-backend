package com.kenkoro.taurus.api.client.routes.cut.order

import com.kenkoro.taurus.api.client.controllers.CutOrderController
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import com.kenkoro.taurus.api.client.models.NewCutOrder
import com.kenkoro.taurus.api.client.routes.util.Validator
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.call
import io.ktor.server.auth.authenticate
import io.ktor.server.request.receiveNullable
import io.ktor.server.response.respond
import io.ktor.server.routing.Route
import io.ktor.server.routing.post

fun Route.addNewCutOrder(
  cutController: CutOrderController,
  config: TokenConfig,
) {
  authenticate(config.authName) {
    post("/add-new/cut-order") {
      val newCutOrder =
        call.receiveNullable<NewCutOrder>() ?: run {
          call.respond(HttpStatusCode.BadRequest)
          return@post
        }

      if (!Validator.isNewCutOrderValid(newCutOrder)) {
        call.respond(HttpStatusCode.Conflict, "Request has black data")
        return@post
      }

      val addedCutOrder = cutController.addNewCutOrder(newCutOrder)
      if (addedCutOrder == null) {
        call.respond(HttpStatusCode.InternalServerError, "Failed to push the new cut order")
        return@post
      }

      call.respond(
        status = HttpStatusCode.Created,
        message = newCutOrder,
      )
    }
  }
}
