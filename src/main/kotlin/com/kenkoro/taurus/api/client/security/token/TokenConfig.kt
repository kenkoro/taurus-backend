package com.kenkoro.taurus.api.client.security.token

data class TokenConfig(
  val audience: String,
  val domain: String,
  val expiresIn: Long,
  val secret: String
)
