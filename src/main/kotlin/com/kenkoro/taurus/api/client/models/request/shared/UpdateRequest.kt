package com.kenkoro.taurus.api.client.models.request.shared

import kotlinx.serialization.Serializable

@Serializable
data class UpdateRequest(
  val updater: String,
  val value: String
)