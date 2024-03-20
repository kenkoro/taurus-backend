package com.kenkoro.taurus.api.client.core.security.hashing

interface HashingService {
  fun hash(password: String, saltLength: Int = 32): SaltedHash

  fun verify(password: String, saltedHash: SaltedHash): Boolean
}