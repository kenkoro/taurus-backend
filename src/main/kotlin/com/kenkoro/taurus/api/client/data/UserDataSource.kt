package com.kenkoro.taurus.api.client.data

import com.kenkoro.taurus.api.client.model.User

interface UserDataSource {
  suspend fun getUserByItsSubject(subject: String): User?
  suspend fun upsertUser(user: User): Boolean
}