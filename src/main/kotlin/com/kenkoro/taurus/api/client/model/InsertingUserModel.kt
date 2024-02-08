package com.kenkoro.taurus.api.client.model

import com.kenkoro.taurus.api.client.model.util.UserRole
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class InsertingUserModel(
  val subject: String,
  val password: String,
  val image: String,
  @SerialName("first_name") val firstName: String,
  @SerialName("last_name") val lastName: String,
  val role: UserRole,
  val salt: String
)

fun InsertingUserModel.toGettingUserModel(): GettingUserModel {
  return GettingUserModel(
    id = -1,
    subject = subject,
    password = password,
    image = image,
    firstName = firstName,
    lastName = lastName,
    role = role,
    salt = salt
  )
}