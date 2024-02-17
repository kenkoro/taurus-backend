package com.kenkoro.taurus.api.client.data.remote

import com.kenkoro.taurus.api.client.data.remote.util.UpdateType
import com.kenkoro.taurus.api.client.model.GettingUserModel
import com.kenkoro.taurus.api.client.model.InsertingUserModel
import com.kenkoro.taurus.api.client.model.util.UserRole

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
    val model = getUserByItsSubject(user)
    when (type) {
      UpdateType.SUBJECT -> {
        db[db.indexOf(model)] = model.copy(
          id = model.id,
          subject = value,
          password = model.password,
          image = model.image,
          firstName = model.firstName,
          lastName = model.lastName,
          role = model.role,
          salt = model.salt
        )
      }

      UpdateType.PASSWORD -> {
        db[db.indexOf(model)] = model.copy(
          id = model.id,
          subject = model.subject,
          password = value,
          image = model.image,
          firstName = model.firstName,
          lastName = model.lastName,
          role = model.role,
          salt = model.salt
        )
      }

      UpdateType.IMAGE -> {
        db[db.indexOf(model)] = model.copy(
          id = model.id,
          subject = model.subject,
          password = model.password,
          image = value,
          firstName = model.firstName,
          lastName = model.lastName,
          role = model.role,
          salt = model.salt
        )
      }

      UpdateType.FIRST_NAME -> {
        db[db.indexOf(model)] = model.copy(
          id = model.id,
          subject = model.subject,
          password = model.password,
          image = model.image,
          firstName = value,
          lastName = model.lastName,
          role = model.role,
          salt = model.salt
        )
      }

      UpdateType.LAST_NAME -> {
        db[db.indexOf(model)] = model.copy(
          id = model.id,
          subject = model.subject,
          password = model.password,
          image = model.image,
          firstName = model.firstName,
          lastName = value,
          role = model.role,
          salt = model.salt
        )
      }

      UpdateType.ROLE -> {
        db[db.indexOf(model)] = model.copy(
          id = model.id,
          subject = model.subject,
          password = model.password,
          image = model.image,
          firstName = model.firstName,
          lastName = model.lastName,
          role = UserRole.valueOf(value),
          salt = model.salt
        )
      }

      else -> throw IllegalArgumentException("Update type is not correct")
    }

    val updatedRows = 1
    return updatedRows
  }

  override suspend fun delete(user: String): Int {
    val wasAcknowledged = db.remove(getUserByItsSubject(user))
    return if (wasAcknowledged) 1 else 0
  }
}
