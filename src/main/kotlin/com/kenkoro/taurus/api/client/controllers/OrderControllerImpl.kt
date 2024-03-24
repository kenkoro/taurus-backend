package com.kenkoro.taurus.api.client.controllers

import com.kenkoro.taurus.api.client.controllers.OrderController.Companion.changedRows
import com.kenkoro.taurus.api.client.controllers.OrderController.Companion.preparedOrderId
import com.kenkoro.taurus.api.client.models.request.order.Order
import com.kenkoro.taurus.api.client.services.PostgresOrderUserCrudService
import com.kenkoro.taurus.api.client.services.util.OrderUpdateType

class OrderControllerImpl(
  private val service: PostgresOrderUserCrudService
) : OrderController {
  override suspend fun create(model: Order): Boolean {
    return service.create(model)
  }

  override suspend fun read(orderId: Int): Order {
    return service.read(orderId)
  }

  override suspend fun readAll(offset: Int, perPage: Int): List<Order> {
    return service.readAll(offset, perPage)
  }

  override suspend fun update(type: OrderUpdateType, value: String): OrderController {
    changedRows = service.update(
      type = type,
      value = value,
      orderId = preparedOrderId
    )
    return this
  }

  override suspend fun delete(): OrderController {
    changedRows = service.delete(preparedOrderId)
    return this
  }
}