package com.kenkoro.taurus.api.client.di

import com.kenkoro.taurus.api.client.data.DataSourceService
import com.kenkoro.taurus.api.client.data.DataSourceType
import com.kenkoro.taurus.api.client.data.remote.FakeUserDataSource
import com.kenkoro.taurus.api.client.data.remote.PostgresUserDataSource
import com.kenkoro.taurus.api.client.data.remote.UserDataSource
import com.kenkoro.taurus.api.client.data.repository.UserRepository
import com.kenkoro.taurus.api.client.data.repository.UserRepositoryImpl
import java.sql.Connection
import java.sql.DriverManager

object ManualDi {
  private fun provideDb(type: DataSourceType): Connection {
    Class.forName("org.postgresql.Driver")
    val (url, user, password) = DataSourceService.credentials(type)
    return DriverManager.getConnection(url, user, password)
  }

  fun providePostgresUserRepository(type: DataSourceType): UserRepository {
    val dbConnection = provideDb(type)
    val postgresUserDataSource: UserDataSource = PostgresUserDataSource(dbConnection)

    return UserRepositoryImpl(postgresUserDataSource)
  }

  fun provideUserRepositoryWithFakeDataSource(): UserRepository {
    return UserRepositoryImpl(FakeUserDataSource())
  }
}