package com.kenkoro.taurus.api.client.security.token

interface TokenService {
  fun generate(config: TokenConfig, vararg claims: TokenClaim): String
}