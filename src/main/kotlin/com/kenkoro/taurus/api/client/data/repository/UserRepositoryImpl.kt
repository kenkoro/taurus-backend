package com.kenkoro.taurus.api.client.data.repository

import com.kenkoro.taurus.api.client.data.remote.UserDataSource
import com.kenkoro.taurus.api.client.data.remote.util.UpdateType
import com.kenkoro.taurus.api.client.data.repository.UserRepository.Companion.changedRows
import com.kenkoro.taurus.api.client.model.GettingUserModel
import com.kenkoro.taurus.api.client.model.InsertingUserModel

class UserRepositoryImpl(
  private val userDataSource: UserDataSource
) : UserRepository {
  override suspend fun getUserByItsSubject(subject: String): GettingUserModel {
    return userDataSource.getUserByItsSubject(subject)
  }

  override suspend fun createUser(model: InsertingUserModel): Boolean {
    return userDataSource.createUser(model)
  }

  override suspend fun update(type: UpdateType, value: String): UserRepository {
    changedRows = userDataSource.update(type, value, UserRepository.preparedUser)
    return this
  }

  override suspend fun delete(): UserRepository {
    changedRows = userDataSource.delete(UserRepository.preparedUser)
    return this
  }
}