package com.kenkoro.taurus.api.client.plugins

import com.kenkoro.taurus.api.client.db.ManualDi.provideDb
import com.kenkoro.taurus.api.client.route.getRootApi
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {
  routing {
    val db = provideDb(isInProduction = false)
    getRootApi(db)
  }
}
