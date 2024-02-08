package com.kenkoro.taurus.api.client.model.request

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
  val subject: String,
  val password: String
)