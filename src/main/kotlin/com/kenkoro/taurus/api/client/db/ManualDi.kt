package com.kenkoro.taurus.api.client.db

import java.sql.Connection
import java.sql.DriverManager

object ManualDi {
  fun provideDb(isInProduction: Boolean): Connection {
    Class.forName("org.postgresql.Driver")
    if (!isInProduction) {
      val testUrl = System.getenv("PSQL_TEST_URL") ?: throw EnvException("Testing url wasn't provided")
      val testUser = System.getenv("PSQL_TEST_USER") ?: throw EnvException("Testing user wasn't provided")
      val testPassword =
        System.getenv("PSQL_TEST_PASSWORD") ?: throw EnvException("Password for testing user wasn't provided")

      return DriverManager.getConnection(testUrl, testUser, testPassword)
    } else {
      val prodUrl = System.getenv("PSQL_DB_URL") ?: throw EnvException("Url wasn't provided")
      val prodUser = System.getenv("PSQL_USER") ?: throw EnvException("User wasn't provided")
      val prodPassword = System.getenv("PSQL_PASSWORD") ?: throw EnvException("Password for the user wasn't provided")

      return DriverManager.getConnection(prodUrl, prodUser, prodPassword)
    }
  }
}