package com.kenkoro.taurus.api.client.controllers

import com.kenkoro.taurus.api.client.services.util.UpdateType
import com.kenkoro.taurus.api.client.models.request.user.Get
import com.kenkoro.taurus.api.client.models.request.user.CreateWithSalt
import com.kenkoro.taurus.api.client.models.util.UserProfile

interface User {
  companion object {
    var preparedSubject: String = ""
    var preparedModel: CreateWithSalt = CreateWithSalt(
      subject = "",
      password = "",
      image = "",
      firstName = "",
      lastName = "",
      email = "",
      profile = UserProfile.Others,
      salt = ""
    )
    var changedRows: Int = 0
  }

  suspend fun subject(subject: String): User {
    preparedSubject = subject
    return this
  }

  suspend fun model(model: CreateWithSalt): User {
    preparedModel = model
    return this
  }

  suspend fun get(): Get {
    return getUserByItsSubject(preparedSubject)
  }

  suspend fun create(): Boolean {
    return createUser(preparedModel)
  }

  suspend fun wasAcknowledged(): Boolean {
    return changedRows > 0
  }

  suspend fun getUserByItsSubject(subject: String): Get

  suspend fun createUser(model: CreateWithSalt): Boolean

  suspend fun delete(): User

  suspend fun update(type: UpdateType, value: String): User
}