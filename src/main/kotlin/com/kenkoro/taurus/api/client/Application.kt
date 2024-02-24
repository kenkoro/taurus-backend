package com.kenkoro.taurus.api.client

import com.kenkoro.taurus.api.client.plugins.*
import com.kenkoro.taurus.api.client.security.token.JwtTokenConfigService
import com.kenkoro.taurus.api.client.security.token.TokenConfig
import io.ktor.server.application.*
import io.ktor.server.netty.*

fun main(args: Array<String>) {
  EngineMain.main(args)
}

private fun Application.configure() {
  val config = TokenConfig(
    audience = environment.config.property("jwt.audience").getString(),
    domain = environment.config.property("jwt.domain").getString(),
    expiresIn = environment.config.property("jwt.expiresIn").getString().toLong(),
    secret = System.getenv("JWT_SECRET") ?: environment.config.property("jwt.secret.test").getString(),
    realm = environment.config.property("jwt.realm").getString()
  )
  JwtTokenConfigService.setUp(config)

  configureSecurity()
  configureMonitoring()
  configureSerialization()
  configureRouting()
}

fun Application.module() {
  SutService.turnOn(isUnderTest = false)
  configure()
}

fun Application.testModule() {
  SutService.turnOn(isUnderTest = true)
  configure()
}