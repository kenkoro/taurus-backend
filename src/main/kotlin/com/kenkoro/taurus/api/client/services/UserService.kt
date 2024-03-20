package com.kenkoro.taurus.api.client.services

import com.kenkoro.taurus.api.client.services.util.UpdateType
import com.kenkoro.taurus.api.client.models.request.user.Get
import com.kenkoro.taurus.api.client.models.request.user.CreateWithSalt

interface UserService {
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

  suspend fun getUserByItsSubject(subject: String): Get

  suspend fun createUser(model: CreateWithSalt): Boolean

  suspend fun update(type: UpdateType, value: String, user: String): Int

  suspend fun delete(user: String): Int
}