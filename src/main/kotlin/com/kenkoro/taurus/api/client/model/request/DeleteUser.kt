package com.kenkoro.taurus.api.client.model.request

import kotlinx.serialization.Serializable

@Serializable
data class DeleteUser(
  val user: String
)
