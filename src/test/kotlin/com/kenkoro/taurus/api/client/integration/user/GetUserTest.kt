package com.kenkoro.taurus.api.client.integration.user

import com.kenkoro.taurus.api.client.annotation.Integration
import com.kenkoro.taurus.api.client.model.request.CreateUserRequest
import com.kenkoro.taurus.api.client.model.request.LoginRequest
import com.kenkoro.taurus.api.client.model.util.UserRole
import com.kenkoro.taurus.api.client.util.TestService
import com.kenkoro.taurus.api.client.util.TestService.User.givenUser
import com.kenkoro.taurus.api.client.util.TestService.configAndEnvironment
import io.ktor.server.testing.*
import kotlin.test.Ignore
import kotlin.test.Test

class GetUserTest {
  @Test
  @Integration
  @Ignore
  fun `should create a new user, login and get this user`() = testApplication {
    configAndEnvironment(this)

    val model = CreateUserRequest(
      subject = "test",
      password = "test",
      image = "",
      firstName = "test",
      lastName = "",
      role = UserRole.ADMIN
    )
    givenUser(model)
    val response = TestService.User.whenUserSignsIn(
      LoginRequest(
        subject = model.subject,
        password = model.password
      )
    )
    /**
     * TODO: Get a token from login and get the user itself (Assert it)
     */
  }
}