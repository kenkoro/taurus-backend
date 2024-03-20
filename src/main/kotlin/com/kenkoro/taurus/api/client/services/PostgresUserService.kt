package com.kenkoro.taurus.api.client.services

import com.kenkoro.taurus.api.client.core.annotations.Warning
import com.kenkoro.taurus.api.client.models.request.user.Get
import com.kenkoro.taurus.api.client.models.request.user.CreateWithSalt
import com.kenkoro.taurus.api.client.models.util.UserRole
import com.kenkoro.taurus.api.client.services.UserService.Companion.FIRST_NAME
import com.kenkoro.taurus.api.client.services.UserService.Companion.ID
import com.kenkoro.taurus.api.client.services.UserService.Companion.IMAGE
import com.kenkoro.taurus.api.client.services.UserService.Companion.LAST_NAME
import com.kenkoro.taurus.api.client.services.UserService.Companion.PASSWORD
import com.kenkoro.taurus.api.client.services.UserService.Companion.ROLE
import com.kenkoro.taurus.api.client.services.UserService.Companion.SALT
import com.kenkoro.taurus.api.client.services.UserService.Companion.SUBJECT
import com.kenkoro.taurus.api.client.services.util.UpdateType
import java.sql.Connection

@Warning("Rewrite sql queries from plain text to functions from Exposed Framework")
class PostgresUserService(
  private val db: Connection
) : UserService {
  companion object {
    const val USER_TABLE = "t_user"
  }

  override suspend fun getUserByItsSubject(subject: String): Get {
    val preparedStatement = db.prepareStatement("SELECT * FROM $USER_TABLE WHERE $SUBJECT = ?")
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
        role = UserRole.valueOf(result.getString(ROLE)),
        salt = result.getString(SALT)
      )
    }

    return users.first()
  }

  override suspend fun createUser(model: CreateWithSalt): Boolean {
    val preparedStatement = db.prepareStatement(
      "INSERT INTO " +
          "$USER_TABLE($SUBJECT, $PASSWORD, $IMAGE, $FIRST_NAME, $LAST_NAME, $ROLE, $SALT)" +
          "VALUES (?, ?, ?, ?, ?, CAST(? AS user_role), ?)"
    )
    preparedStatement.setString(1, model.subject)
    preparedStatement.setString(2, model.password)
    preparedStatement.setString(3, model.image)
    preparedStatement.setString(4, model.firstName)
    preparedStatement.setString(5, model.lastName)
    preparedStatement.setString(6, model.role.name)
    preparedStatement.setString(7, model.salt)

    val updatedRows = preparedStatement.executeUpdate()

    return updatedRows > 0
  }

  override suspend fun update(type: UpdateType, value: String, user: String): Int {
    val preparedStatement = db.prepareStatement(
      "UPDATE $USER_TABLE SET ${type.toSql} = ? WHERE $SUBJECT = ?"
    )
    preparedStatement.setString(1, value)
    preparedStatement.setString(2, user)

    return preparedStatement.executeUpdate()
  }

  override suspend fun delete(user: String): Int {
    val preparedStatement = db.prepareStatement(
      "DELETE FROM $USER_TABLE WHERE $SUBJECT = ?"
    )
    preparedStatement.setString(1, user)

    return preparedStatement.executeUpdate()
  }
}