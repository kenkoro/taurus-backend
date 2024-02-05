package com.kenkoro.taurus.api.client.data

import com.kenkoro.taurus.api.client.exception.EnvException

object DataSourceService {
  fun credentials(dataSourceType: DataSourceType): Triple<String, String, String> {
    return when (dataSourceType) {
      DataSourceType.POSTGRES_TEST -> {
        val url = System.getenv("PSQL_TEST_URL") ?: throw EnvException("Testing url wasn't provided")
        val user = System.getenv("PSQL_TEST_USER") ?: throw EnvException("Testing user wasn't provided")
        val password =
          System.getenv("PSQL_TEST_PASSWORD") ?: throw EnvException("Password for testing user wasn't provided")

        Triple(url, user, password)
      }

      DataSourceType.POSTGRES_PROD -> {
        val url = System.getenv("PSQL_DB_URL") ?: throw EnvException("Url wasn't provided")
        val user = System.getenv("PSQL_USER") ?: throw EnvException("User wasn't provided")
        val password = System.getenv("PSQL_PASSWORD") ?: throw EnvException("Password for the user wasn't provided")

        Triple(url, user, password)
      }
    }
  }
}