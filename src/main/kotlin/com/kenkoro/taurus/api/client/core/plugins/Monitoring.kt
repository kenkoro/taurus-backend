package com.kenkoro.taurus.api.client.core.plugins

import io.ktor.server.application.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.request.*
import org.slf4j.event.Level

fun Application.configureMonitoring() {
  install(CallLogging) {
    level = Level.DEBUG
    filter { call -> call.request.path().startsWith("/orders") }
  }
}
