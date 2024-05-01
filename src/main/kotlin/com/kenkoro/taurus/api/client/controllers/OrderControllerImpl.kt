package com.kenkoro.taurus.api.client.controllers

import com.kenkoro.taurus.api.client.models.orm.NewOrder
import com.kenkoro.taurus.api.client.models.orm.Order
import com.kenkoro.taurus.api.client.services.dao.OrderDaoFacade
import com.kenkoro.taurus.api.client.services.dao.OrderDaoFacadeImpl

class OrderControllerImpl(
  private val dao: OrderDaoFacade = OrderDaoFacadeImpl()
) : OrderController {
  override suspend fun order(id: Int): Order? = dao.order(id)

  override suspend fun addNewOrder(order: NewOrder): Order? = dao.addNewOrder(order)

  override suspend fun deleteOrder(id: Int): Boolean = dao.deleteOrder(id)

  override suspend fun allOrders(): List<Order> = dao.allOrders()

  override suspend fun paginatedOrders(offset: Long, perPage: Int): List<Order> = dao.paginatedOrders(offset, perPage)

  override suspend fun editOrder(id: Int, fields: NewOrder): Boolean = dao.editOrder(id, fields)
}