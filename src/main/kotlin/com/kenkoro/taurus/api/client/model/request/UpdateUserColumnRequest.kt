package com.kenkoro.taurus.api.client.model.request

import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserColumnRequest(
  val value: String
)
