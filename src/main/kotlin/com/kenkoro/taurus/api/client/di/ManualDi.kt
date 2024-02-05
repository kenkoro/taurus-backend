package com.kenkoro.taurus.api.client.di

import com.kenkoro.taurus.api.client.data.DataSourceType
import com.kenkoro.taurus.api.client.data.DataSourceService
import java.sql.Connection
import java.sql.DriverManager

object ManualDi {
  fun provideDb(type: DataSourceType): Connection {
    Class.forName("org.postgresql.Driver")
    val (url, user, password) = DataSourceService.credentials(type)
    return DriverManager.getConnection(url, user, password)
  }
}