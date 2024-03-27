package com.kenkoro.taurus.api.client.routes.util

import com.kenkoro.taurus.api.client.models.request.shared.UpdateRequest
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*

object RouteService {
  suspend fun handleDataParameterAndGetItWithRequest(call: ApplicationCall): Pair<UpdateRequest, String>? {
    val data = call.parameters["data"] ?: return null
    val request = call.receiveNullable<UpdateRequest>() ?: return null

    if (request.value.isBlank()) {
      call.respond(HttpStatusCode.Conflict, "New data are not valid")
      return null
    }

    return Pair(request, data)
  }
}