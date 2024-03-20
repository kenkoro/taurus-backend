package com.kenkoro.taurus.api.client.core.security.hashing

data class SaltedHash(
  val hashedPasswordWithSalt: String,
  val salt: String
)
