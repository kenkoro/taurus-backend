package com.kenkoro.taurus.api.client.services

import com.kenkoro.taurus.api.client.models.request.user.CreateWithSalt
import com.kenkoro.taurus.api.client.models.request.user.Get
import com.kenkoro.taurus.api.client.models.util.UserProfile
import com.kenkoro.taurus.api.client.services.util.UpdateType

class FakeUserService(
  private val db: MutableList<Get> = mutableListOf()
) : UserService {
  override suspend fun getUserByItsSubject(subject: String): Get {
    return db.find { it.subject == subject } ?: throw NoSuchElementException("User with this subject is not found")
  }

  override suspend fun createUser(model: CreateWithSalt): Boolean {
    return db.add(
      Get(
        id = -1,
        subject = model.subject,
        password = model.password,
        image = model.image,
        firstName = model.firstName,
        lastName = model.lastName,
        email = model.email,
        profile = model.profile,
        salt = model.salt
      )
    )
  }

  override suspend fun update(type: UpdateType, value: String, user: String): Int {
    val model = getUserByItsSubject(user)
    when (type) {
      UpdateType.Subject -> {
        db[db.indexOf(model)] = model.copy(
          id = model.id,
          subject = value,
          password = model.password,
          image = model.image,
          firstName = model.firstName,
          lastName = model.lastName,
          email = model.email,
          profile = model.profile,
          salt = model.salt
        )
      }

      UpdateType.Password -> {
        db[db.indexOf(model)] = model.copy(
          id = model.id,
          subject = model.subject,
          password = value,
          image = model.image,
          firstName = model.firstName,
          lastName = model.lastName,
          email = model.email,
          profile = model.profile,
          salt = model.salt
        )
      }

      UpdateType.Image -> {
        db[db.indexOf(model)] = model.copy(
          id = model.id,
          subject = model.subject,
          password = model.password,
          image = value,
          firstName = model.firstName,
          lastName = model.lastName,
          email = model.email,
          profile = model.profile,
          salt = model.salt
        )
      }

      UpdateType.FirstName -> {
        db[db.indexOf(model)] = model.copy(
          id = model.id,
          subject = model.subject,
          password = model.password,
          image = model.image,
          firstName = value,
          lastName = model.lastName,
          email = model.email,
          profile = model.profile,
          salt = model.salt
        )
      }

      UpdateType.LastName -> {
        db[db.indexOf(model)] = model.copy(
          id = model.id,
          subject = model.subject,
          password = model.password,
          image = model.image,
          firstName = model.firstName,
          lastName = value,
          email = model.email,
          profile = model.profile,
          salt = model.salt
        )
      }

      UpdateType.Email -> {
        db[db.indexOf(model)] = model.copy(
          id = model.id,
          subject = model.subject,
          password = model.password,
          image = model.image,
          firstName = model.firstName,
          lastName = model.lastName,
          email = value,
          profile = model.profile,
          salt = model.salt
        )
      }

      UpdateType.Profile -> {
        db[db.indexOf(model)] = model.copy(
          id = model.id,
          subject = model.subject,
          password = model.password,
          image = model.image,
          firstName = model.firstName,
          lastName = model.lastName,
          email = model.email,
          profile = UserProfile.valueOf(value),
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
