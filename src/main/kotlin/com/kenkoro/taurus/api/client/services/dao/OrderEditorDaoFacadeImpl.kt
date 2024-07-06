package com.kenkoro.taurus.api.client.services.dao

import com.kenkoro.taurus.api.client.models.Orders
import com.kenkoro.taurus.api.client.models.Orders.orderId
import com.kenkoro.taurus.api.client.services.DbService.dbQuery
import org.jetbrains.exposed.sql.selectAll

class OrderEditorDaoFacadeImpl : OrderEditorDaoFacade {
  override suspend fun isOrderIdUnique(id: Int): Boolean =
    dbQuery {
      Orders.selectAll().where { orderId eq id }.empty()
    }
}
