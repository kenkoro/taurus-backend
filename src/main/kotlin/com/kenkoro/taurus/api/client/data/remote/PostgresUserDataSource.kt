package com.kenkoro.taurus.api.client.data.remote

import com.kenkoro.taurus.api.client.annotation.Warning
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Companion.FIRST_NAME
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Companion.ID
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Companion.IMAGE
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Companion.LAST_NAME
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Companion.PASSWORD
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Companion.ROLE
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Companion.SALT
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Companion.SUBJECT
import com.kenkoro.taurus.api.client.model.GettingUserModel
import com.kenkoro.taurus.api.client.model.InsertingUserModel
import com.kenkoro.taurus.api.client.model.util.UserRole
import java.sql.Connection

@Warning("Rewrite sql queries from plain text to functions from Exposed Framework")
class PostgresUserDataSource(
  private val db: Connection
) : UserDataSource {
  companion object {
    const val USER_TABLE = "t_user"
  }

  override suspend fun getUserByItsSubject(subject: String): GettingUserModel {
    val preparedStatement = db.prepareStatement("SELECT * FROM $USER_TABLE WHERE $SUBJECT = ?")
    preparedStatement.setString(1, subject)
    val result = preparedStatement.executeQuery()

    val users = mutableListOf<GettingUserModel>()
    while (result.next()) {
      users += GettingUserModel(
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

  override suspend fun createUser(model: InsertingUserModel): Boolean {
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

  override suspend fun updateUserSubject(subject: String, whichUser: String): Boolean {
    val preparedStatement = db.prepareStatement(
      "UPDATE $USER_TABLE SET $SUBJECT = ? WHERE $SUBJECT = ?"
    )
    preparedStatement.setString(1, subject)
    preparedStatement.setString(2, whichUser)

    val updatedRows = preparedStatement.executeUpdate()

    return updatedRows > 0
  }

  override suspend fun updateUserPassword(password: String, whichUser: String): Boolean {
    val preparedStatement = db.prepareStatement(
      "UPDATE $USER_TABLE SET $PASSWORD = ? WHERE $SUBJECT = ?"
    )
    preparedStatement.setString(1, password)
    preparedStatement.setString(2, whichUser)

    val updatedRows = preparedStatement.executeUpdate()

    return updatedRows > 0
  }

  override suspend fun updateUserImage(image: String, whichUser: String): Boolean {
    val preparedStatement = db.prepareStatement(
      "UPDATE $USER_TABLE SET $IMAGE = ? WHERE $SUBJECT = ?"
    )
    preparedStatement.setString(1, image)
    preparedStatement.setString(2, whichUser)

    val updatedRows = preparedStatement.executeUpdate()

    return updatedRows > 0
  }

  override suspend fun updateUserFirstName(firstName: String, whichUser: String): Boolean {
    val preparedStatement = db.prepareStatement(
      "UPDATE $USER_TABLE SET $FIRST_NAME = ? WHERE $SUBJECT = ?"
    )
    preparedStatement.setString(1, firstName)
    preparedStatement.setString(2, whichUser)

    val updatedRows = preparedStatement.executeUpdate()

    return updatedRows > 0
  }

  override suspend fun updateUserLastName(lastName: String, whichUser: String): Boolean {
    val preparedStatement = db.prepareStatement(
      "UPDATE $USER_TABLE SET $LAST_NAME = ? WHERE $SUBJECT = ?"
    )
    preparedStatement.setString(1, lastName)
    preparedStatement.setString(2, whichUser)

    val updatedRows = preparedStatement.executeUpdate()

    return updatedRows > 0
  }

  override suspend fun updateUserRole(role: UserRole, whichUser: String): Boolean {
    val preparedStatement = db.prepareStatement(
      "UPDATE $USER_TABLE SET $ROLE = ? WHERE $SUBJECT = ?"
    )
    preparedStatement.setString(1, role.name)
    preparedStatement.setString(2, whichUser)

    val updatedRows = preparedStatement.executeUpdate()

    return updatedRows > 0
  }

  override suspend fun updateUserSalt(salt: String, whichUser: String): Boolean {
    val preparedStatement = db.prepareStatement(
      "UPDATE $USER_TABLE SET $SALT = ? WHERE $SUBJECT = ?"
    )
    preparedStatement.setString(1, salt)
    preparedStatement.setString(2, whichUser)

    val updatedRows = preparedStatement.executeUpdate()

    return updatedRows > 0
  }
}