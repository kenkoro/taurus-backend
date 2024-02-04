package com.kenkoro.taurus.api.client.plugins

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
  routing {
    get("/") {
      call.respondText(contentType = ContentType.Any, status = HttpStatusCode.OK) { "Hello!" }
    }
  }
}
