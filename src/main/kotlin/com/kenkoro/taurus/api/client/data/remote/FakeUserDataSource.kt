package com.kenkoro.taurus.api.client.data.remote

import com.kenkoro.taurus.api.client.data.remote.util.UpdateType
import com.kenkoro.taurus.api.client.model.GettingUserModel
import com.kenkoro.taurus.api.client.model.InsertingUserModel

class FakeUserDataSource(
  private val db: MutableList<GettingUserModel> = mutableListOf()
) : UserDataSource {
  override suspend fun getUserByItsSubject(subject: String): GettingUserModel {
    return db.find { it.subject == subject } ?: throw NoSuchElementException("User with this subject is not found")
  }

  override suspend fun createUser(model: InsertingUserModel): Boolean {
    return db.add(
      GettingUserModel(
        id = -1,
        subject = model.subject,
        password = model.password,
        image = model.image,
        firstName = model.firstName,
        lastName = model.lastName,
        role = model.role,
        salt = model.salt
      )
    )
  }

  override suspend fun update(type: UpdateType, value: String, user: String): Int {
    TODO("Not yet implemented")
  }

  override suspend fun delete(user: String): Int {
    val wasAcknowledged = db.remove(getUserByItsSubject(user))
    return if (wasAcknowledged) 1 else 0
  }
}
