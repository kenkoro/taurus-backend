package com.kenkoro.taurus.api.client.models.util

import com.kenkoro.taurus.api.client.models.Order
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PaginatedOrders(
  @SerialName("paginated_orders") val paginatedOrders: List<Order>,
  @SerialName("has_next_page") val hasNextPage: Boolean
)
