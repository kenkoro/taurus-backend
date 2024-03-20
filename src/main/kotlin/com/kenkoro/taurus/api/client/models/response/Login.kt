package com.kenkoro.taurus.api.client.models.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
  val token: String
)
