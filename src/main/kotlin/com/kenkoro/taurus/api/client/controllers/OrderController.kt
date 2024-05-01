package com.kenkoro.taurus.api.client.controllers

import com.kenkoro.taurus.api.client.models.orm.NewOrder
import com.kenkoro.taurus.api.client.models.orm.Order
import com.kenkoro.taurus.api.client.services.dao.OrderFields

interface OrderController {
  suspend fun order(id: Int): Order?
  suspend fun addNewOrder(order: NewOrder): Order?
  suspend fun deleteOrder(id: Int): Boolean
  suspend fun allOrders(): List<Order>
  suspend fun paginatedOrders(offset: Long, perPage: Int): List<Order>
  suspend fun editOrder(
    id: Int,
    fields: NewOrder,
  ): Boolean
}