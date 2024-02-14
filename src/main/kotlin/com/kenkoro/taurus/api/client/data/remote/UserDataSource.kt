package com.kenkoro.taurus.api.client.data.remote

import com.kenkoro.taurus.api.client.data.remote.util.UpdateType
import com.kenkoro.taurus.api.client.model.GettingUserModel
import com.kenkoro.taurus.api.client.model.InsertingUserModel

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

  suspend fun update(type: UpdateType, value: String, user: String): Int

  suspend fun delete(user: String): Int
}