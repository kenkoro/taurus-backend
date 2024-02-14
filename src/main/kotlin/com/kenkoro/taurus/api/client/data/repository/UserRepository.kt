package com.kenkoro.taurus.api.client.data.repository

import com.kenkoro.taurus.api.client.data.remote.util.UpdateType
import com.kenkoro.taurus.api.client.model.GettingUserModel
import com.kenkoro.taurus.api.client.model.InsertingUserModel
import com.kenkoro.taurus.api.client.model.util.UserRole

interface UserRepository {
  companion object {
    var preparedUser: String = ""
    var preparedModel: InsertingUserModel = InsertingUserModel(
      subject = "",
      password = "",
      image = "",
      firstName = "",
      lastName = "",
      role = UserRole.OTHERS,
      salt = ""
    )
    var updatedRows: Int = 0
  }

  suspend fun user(subject: String): UserRepository {
    preparedUser = subject
    return this
  }

  suspend fun user(model: InsertingUserModel): UserRepository {
    preparedModel = model
    return this
  }

  suspend fun get(): GettingUserModel {
    return getUserByItsSubject(preparedUser)
  }

  suspend fun create(): Boolean {
    return createUser(preparedModel)
  }

  suspend fun wasAcknowledged(): Boolean {
    return updatedRows > 0
  }

  suspend fun update(type: UpdateType, value: String): UserRepository
  suspend fun getUserByItsSubject(subject: String): GettingUserModel
  suspend fun createUser(model: InsertingUserModel): Boolean
}