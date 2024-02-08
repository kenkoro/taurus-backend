package com.kenkoro.taurus.api.client.data.remote

import com.kenkoro.taurus.api.client.annotation.Warning
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Column.FIRST_NAME
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Column.ID
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Column.IMAGE
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Column.LAST_NAME
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Column.PASSWORD
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Column.ROLE
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Column.SALT
import com.kenkoro.taurus.api.client.data.remote.UserDataSource.Column.SUBJECT
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
    val preparedStatement = db.prepareStatement("SELECT * FROM $USER_TABLE WHERE subject = ?")
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
          "$USER_TABLE(subject, password, image, first_name, last_name, role, salt)" +
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

  override suspend fun updateUserSubject(subject: String): Boolean {
    TODO("Not yet implemented")
  }

  override suspend fun updateUserPassword(password: String): Boolean {
    TODO("Not yet implemented")
  }

  override suspend fun updateUserImage(image: String): Boolean {
    TODO("Not yet implemented")
  }

  override suspend fun updateUserFirstName(firstName: String): Boolean {
    TODO("Not yet implemented")
  }

  override suspend fun updateUserLastName(lastName: String): Boolean {
    TODO("Not yet implemented")
  }

  override suspend fun updateUserRole(role: UserRole): Boolean {
    TODO("Not yet implemented")
  }

  override suspend fun updateUserSalt(salt: String): Boolean {
    TODO("Not yet implemented")
  }
}