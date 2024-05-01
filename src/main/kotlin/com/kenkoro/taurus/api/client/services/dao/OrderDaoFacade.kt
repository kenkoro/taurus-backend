package com.kenkoro.taurus.api.client.services.dao

import com.kenkoro.taurus.api.client.models.NewOrder
import com.kenkoro.taurus.api.client.models.Order

interface OrderDaoFacade {
  suspend fun order(id: Int): Order?
  suspend fun addNewOrder(order: NewOrder): Order?
  suspend fun deleteOrder(id: Int): Boolean
  suspend fun allOrders(): List<Order>
  suspend fun paginatedOrders(offset: Long, perPage: Int): List<Order>
  suspend fun editOrder(orderId: Int, order: NewOrder): Boolean
}