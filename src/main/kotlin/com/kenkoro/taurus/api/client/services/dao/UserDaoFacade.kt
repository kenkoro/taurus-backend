package com.kenkoro.taurus.api.client.services.dao

import com.kenkoro.taurus.api.client.models.orm.NewUser
import com.kenkoro.taurus.api.client.models.orm.User

interface UserDaoFacade {
  suspend fun user(id: Int): User?
  suspend fun addNewUser(user: NewUser): User?
  suspend fun deleteUser(id: Int): Boolean
  suspend fun allUsers(): List<User>
  suspend fun editUser(user: NewUser): User?
}