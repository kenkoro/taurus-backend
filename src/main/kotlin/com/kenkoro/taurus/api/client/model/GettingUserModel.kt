package com.kenkoro.taurus.api.client.model

import com.kenkoro.taurus.api.client.model.util.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GettingUserModel(
  val id: Int,
  var subject: String,
  var password: String,
  var image: String,
  @SerialName("first_name") var firstName: String,
  @SerialName("last_name") var lastName: String,
  var role: UserRole,
  val salt: String
)