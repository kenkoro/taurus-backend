package com.kenkoro.taurus.api.client.data.remote

import com.kenkoro.taurus.api.client.data.UserDataSource
import com.kenkoro.taurus.api.client.model.User
import com.kenkoro.taurus.api.client.model.util.UserRole
import java.sql.Connection

class PostgresUserDataSource(
  private val db: Connection
) : UserDataSource {
  override suspend fun getUserByItsSubject(subject: String): User? {
    /**
     * WARNING: Rewrite sql query to chain functions
     */
    val preparedStatement = db.prepareStatement("SELECT * FROM t_user WHERE subject = ?")
    preparedStatement.setString(1, subject)
    val result = preparedStatement.executeQuery()

    val users = mutableListOf<User>()
    while (result.next()) {
      users += User(
        id = result.getInt(1),
        subject = result.getString(2),
        password = result.getString(3),
        image = result.getString(4),
        firstName = result.getString(5),
        lastName = result.getString(6),
        role = UserRole.valueOf(result.getString(7))
      )
    }

    return users.first()
  }

  override suspend fun upsertUser(user: User): Boolean {
    TODO("Implement an update to psql")
  }
}