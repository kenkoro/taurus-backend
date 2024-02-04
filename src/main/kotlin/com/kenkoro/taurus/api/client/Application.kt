package com.kenkoro.taurus.api.client

import com.kenkoro.taurus.api.client.plugins.*
import io.ktor.server.application.*

fun main(args: Array<String>) {
  io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
  configureSecurity()
  configureMonitoring()
  configureSerialization()
  configureDatabases()
  configureRouting()
}
