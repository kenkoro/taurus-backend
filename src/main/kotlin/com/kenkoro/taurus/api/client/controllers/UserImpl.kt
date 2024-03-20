package com.kenkoro.taurus.api.client.controllers

import com.kenkoro.taurus.api.client.services.UserService
import com.kenkoro.taurus.api.client.services.util.UpdateType
import com.kenkoro.taurus.api.client.controllers.User.Companion.changedRows
import com.kenkoro.taurus.api.client.models.request.user.Get
import com.kenkoro.taurus.api.client.models.request.user.CreateWithSalt

class UserImpl(
  private val userService: UserService
) : User {
  override suspend fun getUserByItsSubject(subject: String): Get {
    return userService.getUserByItsSubject(subject)
  }

  override suspend fun createUser(model: CreateWithSalt): Boolean {
    return userService.createUser(model)
  }

  override suspend fun update(type: UpdateType, value: String): User {
    changedRows = userService.update(type, value, User.preparedSubject)
    return this
  }

  override suspend fun delete(): User {
    changedRows = userService.delete(User.preparedSubject)
    return this
  }
}