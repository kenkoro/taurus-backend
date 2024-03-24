package com.kenkoro.taurus.api.client.models.request.user

import com.kenkoro.taurus.api.client.models.util.UserProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CreateUserWithSalt(
  val subject: String,
  val password: String,
  val image: String,
  @SerialName("first_name") val firstName: String,
  @SerialName("last_name") val lastName: String,
  val email: String,
  val profile: UserProfile,
  val salt: String
)