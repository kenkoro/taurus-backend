package com.kenkoro.taurus.api.client.data.repository

import com.kenkoro.taurus.api.client.model.User

interface UserRepository {
  suspend fun getUserByItsSubject(subject: String): User?
  suspend fun upsertUser(user: User): Boolean
}