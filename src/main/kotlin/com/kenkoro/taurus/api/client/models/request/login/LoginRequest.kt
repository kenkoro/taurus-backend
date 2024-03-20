package com.kenkoro.taurus.api.client.models.request.login

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
  val subject: String,
  val password: String
)