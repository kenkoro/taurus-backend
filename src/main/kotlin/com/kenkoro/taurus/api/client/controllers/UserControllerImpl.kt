package com.kenkoro.taurus.api.client.controllers

import com.kenkoro.taurus.api.client.controllers.UserController.Companion.changedRows
import com.kenkoro.taurus.api.client.controllers.UserController.Companion.preparedSubject
import com.kenkoro.taurus.api.client.models.request.user.CreateUserWithSalt
import com.kenkoro.taurus.api.client.models.request.user.GetUser
import com.kenkoro.taurus.api.client.services.UserCrudService
import com.kenkoro.taurus.api.client.services.util.UserUpdateType

class UserControllerImpl(
  private val service: UserCrudService
) : UserController {
  override suspend fun create(model: CreateUserWithSalt): Boolean {
    return service.create(model)
  }

  override suspend fun read(subject: String): GetUser {
    return service.read(subject)
  }

  override suspend fun update(type: UserUpdateType, value: String): UserController {
    changedRows = service.update(
      type = type,
      value = value,
      subject = preparedSubject
    )
    return this
  }

  override suspend fun delete(): UserController {
    changedRows = service.delete(preparedSubject)
    return this
  }
}