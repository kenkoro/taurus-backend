package com.kenkoro.taurus.api.client.services

import com.kenkoro.taurus.api.client.core.exceptions.EnvException
import com.kenkoro.taurus.api.client.models.orm.Orders
import com.kenkoro.taurus.api.client.services.DbService.credentials
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

@JvmInline
value class PsqlUrl(val value: String)

@JvmInline
value class PsqlUser(val value: String)

@JvmInline
value class PsqlPassword(val value: String)

object DbSettings {
  val db by lazy {
    val (url, user, password) = credentials()
    val driverClassName = "org.postgresql.Driver"

    Database.connect(
      url = url.value,
      driver = driverClassName,
      user = user.value,
      password = password.value,
    )
  }
}

object DbService {
  fun init() {
    val db = DbSettings.db
    transaction(db) {
      SchemaUtils.create(
        Orders,
        // Other tables to init
      )
    }
  }

  suspend fun <T> dbQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO) { block() }

  fun credentials(): Triple<PsqlUrl, PsqlUser, PsqlPassword> {
    val url = System.getenv("PSQL_URL") ?: throw EnvException("Testing url wasn't provided")
    val user = System.getenv("PSQL_USER") ?: throw EnvException("Testing user wasn't provided")
    val password =
      System.getenv("PSQL_PASSWORD") ?: throw EnvException("Password for testing user wasn't provided")

    return Triple(PsqlUrl(url), PsqlUser(user), PsqlPassword(password))
  }
}