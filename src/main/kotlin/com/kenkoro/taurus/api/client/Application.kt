package com.kenkoro.taurus.api.client

import com.kenkoro.taurus.api.client.core.plugins.SutService
import com.kenkoro.taurus.api.client.core.plugins.configureExposed
import com.kenkoro.taurus.api.client.core.plugins.configureMonitoring
import com.kenkoro.taurus.api.client.core.plugins.configureRouting
import com.kenkoro.taurus.api.client.core.plugins.configureSecurity
import com.kenkoro.taurus.api.client.core.plugins.configureSerialization
import com.kenkoro.taurus.api.client.core.security.token.JwtTokenConfigService
import com.kenkoro.taurus.api.client.core.security.token.TokenConfig
import io.ktor.server.application.Application
import io.ktor.server.netty.EngineMain

fun main(args: Array<String>) {
  EngineMain.main(args)
}

private fun Application.configure() {
  val config =
    TokenConfig(
      audience = environment.config.property("jwt.audience").getString(),
      domain = environment.config.property("jwt.domain").getString(),
      expiresIn = environment.config.property("jwt.expiresIn").getString().toLong(),
      secret =
        System.getenv("JWT_SECRET") ?: environment.config.property("jwt.secret.test")
          .getString(),
      realm = environment.config.property("jwt.realm").getString(),
    )
  JwtTokenConfigService.setUp(config)

  configureExposed()
  configureSecurity()
  configureMonitoring()
  configureSerialization()
  configureRouting()
}

fun Application.module() {
  SutService.init(isUnderTest = false)
  configure()
}

fun Application.testModule() {
  SutService.init(isUnderTest = true)
  configure()
}
