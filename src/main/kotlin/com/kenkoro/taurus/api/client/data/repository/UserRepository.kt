package com.kenkoro.taurus.api.client.data.repository

import com.kenkoro.taurus.api.client.model.GettingUserModel
import com.kenkoro.taurus.api.client.model.InsertingUserModel

interface UserRepository {
  suspend fun getUserByItsSubject(subject: String): GettingUserModel
  suspend fun createUser(model: InsertingUserModel): Boolean
}