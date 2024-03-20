package com.kenkoro.taurus.api.client.services

import com.kenkoro.taurus.api.client.core.exceptions.EnvException

object DbService {
  fun credentials(): Triple<String, String, String> {
    val url = System.getenv("PSQL_URL") ?: throw EnvException("Testing url wasn't provided")
    val user = System.getenv("PSQL_USER") ?: throw EnvException("Testing user wasn't provided")
    val password =
      System.getenv("PSQL_PASSWORD") ?: throw EnvException("Password for testing user wasn't provided")

    return Triple(url, user, password)
  }
}