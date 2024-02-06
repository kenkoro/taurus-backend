package com.kenkoro.taurus.api.client.data

import com.kenkoro.taurus.api.client.model.GettingUserModel
import com.kenkoro.taurus.api.client.model.InsertingUserModel

interface UserDataSource {
  companion object Column {
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
}