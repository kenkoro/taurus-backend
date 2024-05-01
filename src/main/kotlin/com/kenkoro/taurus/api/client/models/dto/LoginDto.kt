package com.kenkoro.taurus.api.client.models.dto

import kotlinx.serialization.Serializable

@Serializable
data class LoginDto(
  val subject: String,
  val password: String,
)
