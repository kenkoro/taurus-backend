package com.kenkoro.taurus.api.client.data.repository

import com.kenkoro.taurus.api.client.data.remote.UserDataSource
import com.kenkoro.taurus.api.client.model.GettingUserModel
import com.kenkoro.taurus.api.client.model.InsertingUserModel
import com.kenkoro.taurus.api.client.model.util.UserRole

class UserRepositoryImpl(
  private val userDataSource: UserDataSource
) : UserRepository {
  override suspend fun getUserByItsSubject(subject: String): GettingUserModel {
    return userDataSource.getUserByItsSubject(subject)
  }

  override suspend fun createUser(model: InsertingUserModel): Boolean {
    return userDataSource.createUser(model)
  }

  override suspend fun updateUserSubject(subject: String, whichUser: String): Boolean {
    return userDataSource.updateUserSubject(subject, whichUser)
  }

  override suspend fun updateUserPassword(password: String, whichUser: String): Boolean {
    return userDataSource.updateUserPassword(password, whichUser)
  }

  override suspend fun updateUserImage(image: String, whichUser: String): Boolean {
    return userDataSource.updateUserImage(image, whichUser)
  }

  override suspend fun updateUserFirstName(firstName: String, whichUser: String): Boolean {
    return userDataSource.updateUserFirstName(firstName, whichUser)
  }

  override suspend fun updateUserLastName(lastName: String, whichUser: String): Boolean {
    return userDataSource.updateUserLastName(lastName, whichUser)
  }

  override suspend fun updateUserRole(role: UserRole, whichUser: String): Boolean {
    return userDataSource.updateUserRole(role, whichUser)
  }

  override suspend fun updateUserSalt(salt: String, whichUser: String): Boolean {
    return userDataSource.updateUserSalt(salt, whichUser)
  }
}