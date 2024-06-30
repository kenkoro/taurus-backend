package com.kenkoro.taurus.api.client.controllers

import com.kenkoro.taurus.api.client.models.NewUser
import com.kenkoro.taurus.api.client.models.SaltWrapper
import com.kenkoro.taurus.api.client.models.User
import com.kenkoro.taurus.api.client.services.dao.UserDaoFacade
import com.kenkoro.taurus.api.client.services.dao.UserDaoFacadeImpl

class UserControllerImpl(
  private val dao: UserDaoFacade = UserDaoFacadeImpl(),
) : UserController {
  override suspend fun user(id: Int): User? = dao.user(id)

  override suspend fun user(subject: String): User? = dao.user(subject)

  override suspend fun addNewUser(
    user: NewUser,
    saltWrapper: SaltWrapper,
  ): User? =
    dao.addNewUser(
      user,
      saltWrapper,
    )

  override suspend fun deleteUser(id: Int): Boolean = dao.deleteUser(id)

  override suspend fun deleteUser(subject: String): Boolean = dao.deleteUser(subject)

  override suspend fun allUsers(): List<User> = dao.allUsers()

  override suspend fun editUser(
    subject: String,
    user: NewUser,
  ): Boolean =
    dao.editUser(
      subject,
      user,
    )
}
