package com.kenkoro.taurus.api.client.models.request.shared

import kotlinx.serialization.Serializable

@Serializable
data class DeleteRequest(
  val deleter: String
)
