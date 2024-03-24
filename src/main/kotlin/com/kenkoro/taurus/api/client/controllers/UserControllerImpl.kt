package com.kenkoro.taurus.api.client.controllers

import com.kenkoro.taurus.api.client.controllers.UserController.Companion.changedRows
import com.kenkoro.taurus.api.client.models.request.user.CreateUserWithSalt
import com.kenkoro.taurus.api.client.models.request.user.GetUser
import com.kenkoro.taurus.api.client.services.PostgresUserCrudService
import com.kenkoro.taurus.api.client.services.util.UserUpdateType

class UserControllerImpl(
  private val postgresUserCrudService: PostgresUserCrudService
) : UserController {
  override suspend fun read(subject: String): GetUser {
    return postgresUserCrudService.read(subject)
  }

  override suspend fun create(model: CreateUserWithSalt): Boolean {
    return postgresUserCrudService.create(model)
  }

  override suspend fun update(type: UserUpdateType, value: String): UserController {
    changedRows = postgresUserCrudService.update(
      type = type,
      value = value,
      subject = UserController.preparedSubject
    )
    return this
  }

  override suspend fun delete(): UserController {
    changedRows = postgresUserCrudService.delete(UserController.preparedSubject)
    return this
  }
}