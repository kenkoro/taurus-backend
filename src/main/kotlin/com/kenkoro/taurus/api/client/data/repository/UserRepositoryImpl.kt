package com.kenkoro.taurus.api.client.data.repository

import com.kenkoro.taurus.api.client.data.UserDataSource
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
}