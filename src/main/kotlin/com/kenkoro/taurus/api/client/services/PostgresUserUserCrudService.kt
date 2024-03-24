package com.kenkoro.taurus.api.client.services

import com.kenkoro.taurus.api.client.core.annotations.Warning
import com.kenkoro.taurus.api.client.models.request.user.CreateUserWithSalt
import com.kenkoro.taurus.api.client.models.request.user.GetUser
import com.kenkoro.taurus.api.client.core.mappers.fromResult
import com.kenkoro.taurus.api.client.core.mappers.setValues
import com.kenkoro.taurus.api.client.services.util.UserUpdateType
import java.sql.Connection

@Warning("Rewrite sql queries from plain text to functions from Exposed Framework")
class PostgresUserUserCrudService(
  private val db: Connection
) : UserCrudService {
  companion object {
    const val TABLE = "account"
    const val ID = "id"
    const val SUBJECT = "subject"
    const val PASSWORD = "password"
    const val IMAGE = "image"
    const val FIRST_NAME = "first_name"
    const val LAST_NAME = "last_name"
    const val EMAIL = "email"
    const val PROFILE = "profile"
    const val SALT = "salt"
  }

  override fun create(model: CreateUserWithSalt): Boolean {
    val preparedStatement = db.prepareStatement(
      "INSERT INTO " +
          "$TABLE($SUBJECT, $PASSWORD, $IMAGE, $FIRST_NAME, $LAST_NAME, $EMAIL, $PROFILE, $SALT)" +
          "VALUES (?, ?, ?, ?, ?, ?, CAST(? AS user_role), ?)"
    )
    preparedStatement.setValues(model)

    val updatedRows = preparedStatement.executeUpdate()

    return updatedRows > 0
  }

  override fun read(subject: String): GetUser {
    val preparedStatement = db.prepareStatement("SELECT * FROM $TABLE WHERE $SUBJECT = ?")
    preparedStatement.setString(1, subject)
    val result = preparedStatement.executeQuery()

    val users = mutableListOf<GetUser>()
    while (result.next()) {
      users += GetUser.fromResult(result)
    }

    return users.first()
  }

  override fun update(type: UserUpdateType, value: String, subject: String): Int {
    val preparedStatement = db.prepareStatement(
      "UPDATE $TABLE SET ${type.toSql} = ? WHERE $SUBJECT = ?"
    )
    preparedStatement.setString(1, value)
    preparedStatement.setString(2, subject)

    return preparedStatement.executeUpdate()
  }

  override fun delete(subject: String): Int {
    val preparedStatement = db.prepareStatement(
      "DELETE FROM $TABLE WHERE $SUBJECT = ?"
    )
    preparedStatement.setString(1, subject)

    return preparedStatement.executeUpdate()
  }
}