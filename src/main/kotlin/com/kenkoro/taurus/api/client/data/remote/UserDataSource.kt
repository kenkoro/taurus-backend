package com.kenkoro.taurus.api.client.data.remote

import com.kenkoro.taurus.api.client.model.GettingUserModel
import com.kenkoro.taurus.api.client.model.InsertingUserModel
import com.kenkoro.taurus.api.client.model.util.UserRole

interface UserDataSource {
  companion object {
    const val ID = "id"
    const val SUBJECT = "subject"
    const val PASSWORD = "password"
    const val IMAGE = "image"
    const val FIRST_NAME = "first_name"
    const val LAST_NAME = "last_name"
    const val ROLE = "role"
    const val SALT = "salt"
  }

  suspend fun getUserByItsSubject(subject: String): GettingUserModel
  suspend fun createUser(insertingUser: InsertingUserModel): Boolean

  suspend fun updateUserSubject(subject: String, whichUser: String): Boolean

  suspend fun updateUserPassword(password: String, whichUser: String): Boolean

  suspend fun updateUserImage(image: String, whichUser: String): Boolean

  suspend fun updateUserFirstName(firstName: String, whichUser: String): Boolean

  suspend fun updateUserLastName(lastName: String, whichUser: String): Boolean

  suspend fun updateUserRole(role: UserRole, whichUser: String): Boolean

  suspend fun updateUserSalt(salt: String, whichUser: String): Boolean
}