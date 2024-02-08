package com.kenkoro.taurus.api.client.model

import com.kenkoro.taurus.api.client.model.util.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GettingUserModel(
  val id: Int,
  val subject: String,
  val password: String,
  val image: String,
  @SerialName("first_name") val firstName: String,
  @SerialName("last_name") val lastName: String,
  val role: UserRole,
  val salt: String
)