package com.kenkoro.taurus.api.client.models.response

import kotlinx.serialization.Serializable

@Serializable
data class LoginResponse(
  val token: String
)
