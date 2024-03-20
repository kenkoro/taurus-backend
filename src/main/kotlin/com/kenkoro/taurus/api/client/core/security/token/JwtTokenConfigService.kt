package com.kenkoro.taurus.api.client.core.security.token

object JwtTokenConfigService {
  private lateinit var config: TokenConfig

  fun setUp(config: TokenConfig) {
    JwtTokenConfigService.config = config
  }

  fun config(): TokenConfig = config
}