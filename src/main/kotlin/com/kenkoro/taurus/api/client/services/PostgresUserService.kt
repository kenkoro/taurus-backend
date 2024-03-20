package com.kenkoro.taurus.api.client.services

import com.kenkoro.taurus.api.client.core.annotations.Warning
import com.kenkoro.taurus.api.client.models.request.user.Get
import com.kenkoro.taurus.api.client.models.request.user.CreateWithSalt
import com.kenkoro.taurus.api.client.models.util.UserProfile
import com.kenkoro.taurus.api.client.services.UserService.Companion.EMAIL
import com.kenkoro.taurus.api.client.services.UserService.Companion.FIRST_NAME
import com.kenkoro.taurus.api.client.services.UserService.Companion.ID
import com.kenkoro.taurus.api.client.services.UserService.Companion.IMAGE
import com.kenkoro.taurus.api.client.services.UserService.Companion.LAST_NAME
import com.kenkoro.taurus.api.client.services.UserService.Companion.PASSWORD
import com.kenkoro.taurus.api.client.services.UserService.Companion.PROFILE
import com.kenkoro.taurus.api.client.services.UserService.Companion.SALT
import com.kenkoro.taurus.api.client.services.UserService.Companion.SUBJECT
import com.kenkoro.taurus.api.client.services.util.UpdateType
import java.sql.Connection

@Warning("Rewrite sql queries from plain text to functions from Exposed Framework")
class PostgresUserService(
  private val db: Connection
) : UserService {
  companion object {
    const val ACCOUNT = "account"
  }

  override suspend fun getUserByItsSubject(subject: String): Get {
    val preparedStatement = db.prepareStatement("SELECT * FROM $ACCOUNT WHERE $SUBJECT = ?")
    preparedStatement.setString(1, subject)
    val result = preparedStatement.executeQuery()

    val users = mutableListOf<Get>()
    while (result.next()) {
      users += Get(
        id = result.getInt(ID),
        subject = result.getString(SUBJECT),
        password = result.getString(PASSWORD),
        image = result.getString(IMAGE),
        firstName = result.getString(FIRST_NAME),
        lastName = result.getString(LAST_NAME),
        email = result.getString(EMAIL),
        profile = UserProfile.valueOf(result.getString(PROFILE)),
        salt = result.getString(SALT)
      )
    }

    return users.first()
  }

  override suspend fun createUser(model: CreateWithSalt): Boolean {
    val preparedStatement = db.prepareStatement(
      "INSERT INTO " +
          "$ACCOUNT($SUBJECT, $PASSWORD, $IMAGE, $FIRST_NAME, $LAST_NAME, $EMAIL, $PROFILE, $SALT)" +
          "VALUES (?, ?, ?, ?, ?, ?, CAST(? AS user_role), ?)"
    )
    preparedStatement.setString(1, model.subject)
    preparedStatement.setString(2, model.password)
    preparedStatement.setString(3, model.image)
    preparedStatement.setString(4, model.firstName)
    preparedStatement.setString(5, model.lastName)
    preparedStatement.setString(6, model.email)
    preparedStatement.setString(7, model.profile.name)
    preparedStatement.setString(8, model.salt)

    val updatedRows = preparedStatement.executeUpdate()

    return updatedRows > 0
  }

  override suspend fun update(type: UpdateType, value: String, user: String): Int {
    val preparedStatement = db.prepareStatement(
      "UPDATE $ACCOUNT SET ${type.toSql} = ? WHERE $SUBJECT = ?"
    )
    preparedStatement.setString(1, value)
    preparedStatement.setString(2, user)

    return preparedStatement.executeUpdate()
  }

  override suspend fun delete(user: String): Int {
    val preparedStatement = db.prepareStatement(
      "DELETE FROM $ACCOUNT WHERE $SUBJECT = ?"
    )
    preparedStatement.setString(1, user)

    return preparedStatement.executeUpdate()
  }
}