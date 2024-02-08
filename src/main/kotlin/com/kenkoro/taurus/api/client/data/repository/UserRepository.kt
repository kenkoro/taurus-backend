package com.kenkoro.taurus.api.client.data.repository

import com.kenkoro.taurus.api.client.model.GettingUserModel
import com.kenkoro.taurus.api.client.model.InsertingUserModel
import com.kenkoro.taurus.api.client.model.util.UserRole

interface UserRepository {
  companion object {
    var preparedUser: String = ""
  }

  suspend fun user(subject: String): UserRepository {
    preparedUser = subject
    return this
  }

  suspend fun get(): GettingUserModel {
    return getUserByItsSubject(preparedUser)
  }

  suspend fun updateSubject(subject: String): Boolean {
    return updateUserSubject(subject, preparedUser)
  }

  suspend fun updatePassword(password: String): Boolean {
    return updateUserPassword(password, preparedUser)
  }

  suspend fun updateImage(image: String): Boolean {
    return updateUserImage(image, preparedUser)
  }

  suspend fun updateFirstName(firstName: String): Boolean {
    return updateUserFirstName(firstName, preparedUser)
  }

  suspend fun updateLastName(lastName: String): Boolean {
    return updateUserLastName(lastName, preparedUser)
  }

  suspend fun updateRole(role: UserRole): Boolean {
    return updateUserRole(role, preparedUser)
  }

  suspend fun updateSalt(salt: String): Boolean {
    return updateUserSalt(salt, preparedUser)
  }

  suspend fun getUserByItsSubject(subject: String): GettingUserModel
  suspend fun createUser(model: InsertingUserModel): Boolean

  suspend fun updateUserSubject(subject: String, whichUser: String): Boolean

  suspend fun updateUserPassword(password: String, whichUser: String): Boolean

  suspend fun updateUserImage(image: String, whichUser: String): Boolean

  suspend fun updateUserFirstName(firstName: String, whichUser: String): Boolean

  suspend fun updateUserLastName(lastName: String, whichUser: String): Boolean

  suspend fun updateUserRole(role: UserRole, whichUser: String): Boolean

  suspend fun updateUserSalt(salt: String, whichUser: String): Boolean
}