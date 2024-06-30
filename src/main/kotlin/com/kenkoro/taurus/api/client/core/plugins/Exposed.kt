package com.kenkoro.taurus.api.client.core.plugins

import com.kenkoro.taurus.api.client.services.DbService

fun configureExposed() {
  DbService.init()
}
