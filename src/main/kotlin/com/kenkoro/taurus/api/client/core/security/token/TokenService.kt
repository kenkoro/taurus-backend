package com.kenkoro.taurus.api.client.core.security.token

interface TokenService {
  fun generate(
    config: TokenConfig,
    vararg claims: TokenClaim,
  ): String
}
