package com.kenkoro.taurus.api.client.models.response.order

import com.kenkoro.taurus.api.client.models.request.order.Order
import kotlinx.serialization.Serializable

@Serializable
data class GetOrders(
  val paginatedOrders: List<Order>,
  val hasNextPage: Boolean
)