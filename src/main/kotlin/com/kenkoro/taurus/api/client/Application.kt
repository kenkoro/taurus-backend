package com.kenkoro.taurus.api.client

import com.kenkoro.taurus.api.client.plugins.configureMonitoring
import com.kenkoro.taurus.api.client.plugins.configureAuthRouting
import com.kenkoro.taurus.api.client.plugins.configureSecurity
import com.kenkoro.taurus.api.client.plugins.configureSerialization
import io.ktor.server.application.*

fun main(args: Array<String>) {
  io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
  configureSecurity()
  configureMonitoring()
  configureSerialization()
  configureAuthRouting()
}