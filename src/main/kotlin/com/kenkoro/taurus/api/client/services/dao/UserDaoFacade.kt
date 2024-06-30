package com.kenkoro.taurus.api.client.services.dao

import com.kenkoro.taurus.api.client.models.NewUser
import com.kenkoro.taurus.api.client.models.SaltWrapper
import com.kenkoro.taurus.api.client.models.User

interface UserDaoFacade {
  suspend fun user(id: Int): User?

  suspend fun user(subject: String): User?

  suspend fun addNewUser(
    user: NewUser,
    saltWrapper: SaltWrapper,
  ): User?

  suspend fun deleteUser(id: Int): Boolean

  suspend fun deleteUser(subject: String): Boolean

  suspend fun allUsers(): List<User>

  suspend fun editUser(
    subject: String,
    user: NewUser,
  ): Boolean
}
