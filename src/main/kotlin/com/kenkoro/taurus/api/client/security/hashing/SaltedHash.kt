package com.kenkoro.taurus.api.client.security.hashing

data class SaltedHash(
  val hashedPasswordWithSalt: String,
  val salt: String
)
