package com.kenkoro.taurus.api.client

import com.kenkoro.taurus.api.client.exception.EnvException
import com.kenkoro.taurus.api.client.plugins.*
import com.kenkoro.taurus.api.client.security.token.TokenConfig
import io.ktor.server.application.*

fun main(args: Array<String>) {
  io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
  val config = TokenConfig(
    audience = environment.config.property("jwt.audience").getString(),
    domain = environment.config.property("jwt.domain").getString(),
    expiresIn = environment.config.property("jwt.expiresIn").getString().toLong(),
    secret = System.getenv("JWT_SECRET") ?: throw EnvException("The secret jwt key was not provided"),
    realm = environment.config.property("jwt.realm").getString()
  )

  configureSecurity(config)
  configureMonitoring()
  configureSerialization()
  configureAuthRouting(config)
}