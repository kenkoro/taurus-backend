package com.kenkoro.taurus.api.client.models.request.order

import com.kenkoro.taurus.api.client.models.util.OrderStatus
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Order(
  @SerialName("order_id") val orderId: Int,
  val customer: String,
  val date: String,
  val title: String,
  val model: String,
  val size: String,
  val color: String,
  val category: String,
  val quantity: Int,
  val status: OrderStatus
)
