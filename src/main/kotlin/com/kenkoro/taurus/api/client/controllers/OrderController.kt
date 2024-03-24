package com.kenkoro.taurus.api.client.controllers

import com.kenkoro.taurus.api.client.models.request.order.Order
import com.kenkoro.taurus.api.client.models.util.OrderStatus
import com.kenkoro.taurus.api.client.services.util.OrderUpdateType

interface OrderController {
  companion object {
    var preparedOrderId: Int = 0
    var preparedModel: Order = Order(
      orderId = -1,
      customer = "",
      date = "",
      title = "",
      model = "",
      size = "",
      color = "",
      category = "",
      quantity = 0,
      status = OrderStatus.NotStarted
    )
    var changedRows: Int = 0
  }

  fun orderId(orderId: Int): OrderController {
    preparedOrderId = orderId
    return this
  }

  fun model(model: Order): OrderController {
    preparedModel = model
    return this
  }

  fun wasAcknowledged(): Boolean {
    return changedRows > 0
  }

  suspend fun create(model: Order): Boolean

  suspend fun read(orderId: Int): List<Order>

  suspend fun update(type: OrderUpdateType, value: String): OrderController

  suspend fun delete(): OrderController

  suspend fun read(): List<Order> {
    return read(preparedOrderId)
  }

  suspend fun create(): Boolean {
    return create(preparedModel)
  }
}