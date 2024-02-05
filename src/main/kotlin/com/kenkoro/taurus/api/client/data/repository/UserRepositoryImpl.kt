package com.kenkoro.taurus.api.client.data.repository

import com.kenkoro.taurus.api.client.data.UserDataSource
import com.kenkoro.taurus.api.client.model.User

class UserRepositoryImpl(
  private val userDataSource: UserDataSource
) : UserRepository {
  override suspend fun getUserByItsSubject(subject: String): User? {
    return userDataSource.getUserByItsSubject(subject)
  }

  override suspend fun upsertUser(user: User): Boolean {
    return userDataSource.upsertUser(user)
  }
}