package com.kenkoro.taurus.api.client.models.request.user

import kotlinx.serialization.Serializable

@Serializable
data class Delete(
  val user: String
)
