package com.kenkoro.taurus.api.client.di

import com.kenkoro.taurus.api.client.DataSourceType
import com.kenkoro.taurus.api.client.data.DataSourceManager
import java.sql.Connection
import java.sql.DriverManager

object ManualDi {
  fun provideDb(type: DataSourceType): Connection {
    Class.forName("org.postgresql.Driver")
    val (url, user, password) = DataSourceManager.credentials(type)
    return DriverManager.getConnection(url, user, password)
  }
}