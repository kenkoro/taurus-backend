package com.kenkoro.taurus.api.client.services.crud.user

import com.kenkoro.taurus.api.client.models.request.user.CreateUserWithSalt
import com.kenkoro.taurus.api.client.models.request.user.GetUser
import com.kenkoro.taurus.api.client.services.util.UserUpdateType

interface UserCrudService {
  fun create(model: CreateUserWithSalt): Boolean

  fun read(subject: String): GetUser

  fun update(type: UserUpdateType, value: String, subject: String): Int

  fun delete(subject: String): Int
}