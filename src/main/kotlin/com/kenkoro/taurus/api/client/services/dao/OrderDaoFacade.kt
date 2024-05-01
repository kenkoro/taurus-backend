package com.kenkoro.taurus.api.client.services.dao

import com.kenkoro.taurus.api.client.models.orm.Order
import com.kenkoro.taurus.api.client.models.orm.OrderStatus

data class OrderFields(
  val customer: String,
  val title: String,
  val model: String,
  val size: String,
  val color: String,
  val category: String,
  val quantity: Int,
  val status: OrderStatus,
)

interface OrderDaoFacade {
  suspend fun order(id: Int): Order?
  suspend fun addNewOrder(id: Int, fields: OrderFields): Order?
  suspend fun deleteOrder(id: Int): Boolean
  suspend fun allOrders(): List<Order>
  suspend fun paginatedOrders(offset: Long, perPage: Int): List<Order>
  suspend fun editOrder(
    id: Int,
    fields: OrderFields,
  ): Boolean
}