package com.kenkoro.taurus.api.client.services

import com.kenkoro.taurus.api.client.models.request.user.CreateUserWithSalt
import com.kenkoro.taurus.api.client.models.request.user.GetUser
import com.kenkoro.taurus.api.client.models.util.UserProfile
import com.kenkoro.taurus.api.client.services.util.UserUpdateType

class FakeUserCrudService(
  private val db: MutableList<GetUser> = mutableListOf()
) : UserCrudService {
  override fun read(subject: String): GetUser {
    return db.find { it.subject == subject } ?: throw NoSuchElementException("User with this subject is not found")
  }

  override fun create(model: CreateUserWithSalt): Boolean {
    return db.add(
      GetUser(
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

  override fun update(type: UserUpdateType, value: String, subject: String): Int {
    val model = read(subject)
    when (type) {
      UserUpdateType.Subject -> {
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

      UserUpdateType.Password -> {
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

      UserUpdateType.Image -> {
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

      UserUpdateType.FirstName -> {
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

      UserUpdateType.LastName -> {
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

      UserUpdateType.Email -> {
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

      UserUpdateType.Profile -> {
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

  override fun delete(subject: String): Int {
    val wasAcknowledged = db.remove(read(subject))
    return if (wasAcknowledged) 1 else 0
  }
}
