package com.kenkoro.taurus.api.client.plugins

import io.ktor.server.application.*

fun Application.configureRouting() {
  configureAuthRouting()
  configureUserRouting()
}