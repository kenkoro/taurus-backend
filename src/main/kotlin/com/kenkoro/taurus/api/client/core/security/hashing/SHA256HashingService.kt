package com.kenkoro.taurus.api.client.core.security.hashing

import org.apache.commons.codec.binary.Hex
import org.apache.commons.codec.digest.DigestUtils
import java.security.SecureRandom

object HashingAlgorithm {
  const val SHA1PRNG = "SHA1PRNG"
}

class SHA256HashingService : HashingService {
  override fun hash(
    password: String,
    saltLength: Int,
  ): SaltedHash {
    val salt = SecureRandom.getInstance(HashingAlgorithm.SHA1PRNG).generateSeed(saltLength)
    val saltAsHex = Hex.encodeHexString(salt)
    val hash = DigestUtils.sha256Hex("${saltAsHex}$password")

    return SaltedHash(
      hashedPasswordWithSalt = hash,
      salt = saltAsHex,
    )
  }

  override fun verify(
    password: String,
    saltedHash: SaltedHash,
  ): Boolean {
    val hash = DigestUtils.sha256Hex("${saltedHash.salt}$password")
    return hash == saltedHash.hashedPasswordWithSalt
  }
}
