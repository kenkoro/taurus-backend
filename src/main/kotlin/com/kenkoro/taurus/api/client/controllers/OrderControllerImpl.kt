package com.kenkoro.taurus.api.client.controllers

import com.kenkoro.taurus.api.client.models.EditOrder
import com.kenkoro.taurus.api.client.models.NewOrder
import com.kenkoro.taurus.api.client.models.Order
import com.kenkoro.taurus.api.client.services.dao.OrderDaoFacade
import com.kenkoro.taurus.api.client.services.dao.OrderDaoFacadeImpl

class OrderControllerImpl(
  private val dao: OrderDaoFacade = OrderDaoFacadeImpl(),
) : OrderController {
  override suspend fun order(id: Int): Order? = dao.order(id)

  override suspend fun addNewOrder(order: NewOrder): Order? = dao.addNewOrder(order)

  override suspend fun deleteOrder(id: Int): Boolean = dao.deleteOrder(id)

  override suspend fun allOrders(): List<Order> = dao.allOrders()

  override suspend fun paginatedOrders(
    offset: Long,
    perPage: Int,
    phrase: String,
  ): List<Order> =
    dao.paginatedOrders(
      offset = offset,
      perPage = perPage,
      phrase = phrase,
    )

  override suspend fun editOrder(order: EditOrder): Boolean = dao.editOrder(order)
}
