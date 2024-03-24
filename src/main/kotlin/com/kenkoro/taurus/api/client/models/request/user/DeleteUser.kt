package com.kenkoro.taurus.api.client.models.request.user

import kotlinx.serialization.Serializable

@Serializable
data class DeleteUser(
  val user: String
)
