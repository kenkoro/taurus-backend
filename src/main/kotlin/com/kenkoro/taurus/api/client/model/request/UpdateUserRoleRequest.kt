package com.kenkoro.taurus.api.client.model.request

import com.kenkoro.taurus.api.client.model.util.UserRole
import kotlinx.serialization.Serializable

@Serializable
data class UpdateUserRoleRequest(
  val role: UserRole
)
