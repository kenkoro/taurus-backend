package com.kenkoro.taurus.api.client.models.request.order

import kotlinx.serialization.Serializable

@Serializable
data class DeleteOrder(
  val deleter: String
)
