package com.kenkoro.taurus.api.client.models.request.auth

import kotlinx.serialization.Serializable

@Serializable
data class Login(
  val subject: String,
  val password: String
)