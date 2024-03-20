package com.kenkoro.taurus.api.client.models.request.user

import com.kenkoro.taurus.api.client.models.util.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateWithSalt(
  val subject: String,
  val password: String,
  val image: String,
  @SerialName("first_name") val firstName: String,
  @SerialName("last_name") val lastName: String,
  val role: UserRole,
  val salt: String
)