package com.kenkoro.taurus.api.client.route

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import java.sql.Connection

fun Route.getRootApi(db: Connection) {
  get("/") {
    call.respondText { "Works" }
  }
}