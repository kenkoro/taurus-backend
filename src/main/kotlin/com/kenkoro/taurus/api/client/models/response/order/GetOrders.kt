package com.kenkoro.taurus.api.client.models.response.order

import com.kenkoro.taurus.api.client.models.request.order.Order
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetOrders(
  @SerialName("paginated_orders") val paginatedOrders: List<Order>,
  @SerialName("has_next_page") val hasNextPage: Boolean
)