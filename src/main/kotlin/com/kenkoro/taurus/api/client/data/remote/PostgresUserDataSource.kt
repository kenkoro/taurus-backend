package com.kenkoro.taurus.api.client.data.remote

import com.kenkoro.taurus.api.client.data.UserDataSource
import com.kenkoro.taurus.api.client.data.UserDataSource.Column.FIRST_NAME
import com.kenkoro.taurus.api.client.data.UserDataSource.Column.ID
import com.kenkoro.taurus.api.client.data.UserDataSource.Column.IMAGE
import com.kenkoro.taurus.api.client.data.UserDataSource.Column.LAST_NAME
import com.kenkoro.taurus.api.client.data.UserDataSource.Column.PASSWORD
import com.kenkoro.taurus.api.client.data.UserDataSource.Column.ROLE
import com.kenkoro.taurus.api.client.data.UserDataSource.Column.SALT
import com.kenkoro.taurus.api.client.data.UserDataSource.Column.SUBJECT
import com.kenkoro.taurus.api.client.model.GettingUserModel
import com.kenkoro.taurus.api.client.model.InsertingUserModel
import com.kenkoro.taurus.api.client.model.util.UserRole
import java.sql.Connection

class PostgresUserDataSource(
  private val db: Connection
) : UserDataSource {
  /**
   * WARNING: Rewrite sql queries to chain functions
   */
  override suspend fun getUserByItsSubject(subject: String): GettingUserModel {
    val preparedStatement = db.prepareStatement("SELECT * FROM t_user WHERE subject = ?")
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
          "t_user(subject, password, image, first_name, last_name, role, salt)" +
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
}