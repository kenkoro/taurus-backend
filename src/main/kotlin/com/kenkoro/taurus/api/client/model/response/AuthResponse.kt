package com.kenkoro.taurus.api.client.model.response

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
  val token: String
)
