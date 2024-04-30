package com.kenkoro.taurus.api.client.controllers

import com.kenkoro.taurus.api.client.models.request.user.CreateUserWithSalt
import com.kenkoro.taurus.api.client.models.request.user.GetUser
import com.kenkoro.taurus.api.client.models.enums.UserProfile
import com.kenkoro.taurus.api.client.services.util.UserUpdateType

interface UserController {
  companion object {
    var preparedSubject: String = ""
    var preparedModel: CreateUserWithSalt = CreateUserWithSalt(
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

  fun subject(subject: String): UserController {
    preparedSubject = subject
    return this
  }

  fun model(model: CreateUserWithSalt): UserController {
    preparedModel = model
    return this
  }

  fun wasAcknowledged(): Boolean {
    return changedRows > 0
  }

  suspend fun create(model: CreateUserWithSalt): Boolean

  suspend fun read(subject: String): GetUser

  suspend fun update(type: UserUpdateType, value: String): UserController

  suspend fun delete(): UserController

  suspend fun read(): GetUser {
    return read(preparedSubject)
  }

  suspend fun create(): Boolean {
    return create(preparedModel)
  }
}