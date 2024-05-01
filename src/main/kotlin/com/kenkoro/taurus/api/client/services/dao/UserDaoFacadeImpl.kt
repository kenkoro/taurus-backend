package com.kenkoro.taurus.api.client.services.dao

import com.kenkoro.taurus.api.client.models.orm.NewUser
import com.kenkoro.taurus.api.client.models.orm.SaltWrapper
import com.kenkoro.taurus.api.client.models.orm.User
import com.kenkoro.taurus.api.client.models.orm.Users
import org.jetbrains.exposed.sql.ResultRow

class UserDaoFacadeImpl : UserDaoFacade {
  private fun resultRowToUser(row: ResultRow) = User(
    userId = row[Users.userId],
    subject = row[Users.subject],
    password = row[Users.password],
    image = row[Users.image],
    firstName = row[Users.firstName],
    lastName = row[Users.lastName],
    email = row[Users.email],
    profile = row[Users.profile],
    salt = SaltWrapper(row[Users.salt]),
  )

  override suspend fun user(id: Int): User? {
    TODO("Not yet implemented")
  }

  override suspend fun addNewUser(user: NewUser): User? {
    TODO("Not yet implemented")
  }

  override suspend fun deleteUser(id: Int): Boolean {
    TODO("Not yet implemented")
  }

  override suspend fun allUsers(): List<User> {
    TODO("Not yet implemented")
  }

  override suspend fun editUser(user: NewUser): User? {
    TODO("Not yet implemented")
  }
}