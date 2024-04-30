package com.kenkoro.taurus.api.client.models.request.user

import com.kenkoro.taurus.api.client.models.enums.UserProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetUser(
  val id: Int,
  var subject: String,
  var password: String,
  var image: String,
  @SerialName("first_name") var firstName: String,
  @SerialName("last_name") var lastName: String,
  val email: String,
  var profile: UserProfile,
  val salt: String
)