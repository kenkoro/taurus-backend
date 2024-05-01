package com.kenkoro.taurus.api.client.services.dao

import com.kenkoro.taurus.api.client.models.NewUser
import com.kenkoro.taurus.api.client.models.SaltWrapper
import com.kenkoro.taurus.api.client.models.User
import com.kenkoro.taurus.api.client.models.Users
import com.kenkoro.taurus.api.client.models.Users.email
import com.kenkoro.taurus.api.client.models.Users.firstName
import com.kenkoro.taurus.api.client.models.Users.image
import com.kenkoro.taurus.api.client.models.Users.lastName
import com.kenkoro.taurus.api.client.models.Users.password
import com.kenkoro.taurus.api.client.models.Users.profile
import com.kenkoro.taurus.api.client.models.Users.salt
import com.kenkoro.taurus.api.client.models.Users.subject
import com.kenkoro.taurus.api.client.models.Users.userId
import com.kenkoro.taurus.api.client.services.DbService.dbQuery
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.statements.UpdateBuilder

class UserDaoFacadeImpl : UserDaoFacade {
  private fun resultRowToUser(row: ResultRow) = User(
    userId = row[userId],
    subject = row[subject],
    password = row[password],
    image = row[image],
    firstName = row[firstName],
    lastName = row[lastName],
    email = row[email],
    profile = row[profile],
    saltWrapper = SaltWrapper(row[salt]),
  )

  private fun UpdateBuilder<*>.setUserFields(user: NewUser) {
    this[subject] = user.subject
    this[password] = user.password
    this[image] = user.image
    this[firstName] = user.firstName
    this[lastName] = user.lastName
    this[email] = user.email
    this[profile] = user.profile
  }

  override suspend fun user(id: Int): User? = dbQuery {
    Users
      .selectAll()
      .where { userId eq id }
      .map(::resultRowToUser)
      .singleOrNull()
  }

  override suspend fun user(subject: String): User? = dbQuery {
    Users
      .selectAll()
      .where { Users.subject eq subject }
      .map(::resultRowToUser)
      .singleOrNull()
  }

  override suspend fun addNewUser(user: NewUser, saltWrapper: SaltWrapper): User? = dbQuery {
    val insertStatement = Users.insertIgnore {
      it[salt] = saltWrapper.salt
      it.setUserFields(user)
    }
    insertStatement.resultedValues?.singleOrNull()?.let(::resultRowToUser)
  }

  override suspend fun deleteUser(id: Int): Boolean = dbQuery {
    Users.deleteWhere { userId eq id } > 0
  }

  override suspend fun deleteUser(subject: String): Boolean = dbQuery {
    Users.deleteWhere { Users.subject eq subject } > 0
  }

  override suspend fun allUsers(): List<User> = dbQuery {
    Users.selectAll().map(::resultRowToUser)
  }

  override suspend fun editUser(subject: String, user: NewUser): Boolean = dbQuery {
    Users.update({ Users.subject eq subject }) {
      it.setUserFields(user)
    } > 0
  }
}