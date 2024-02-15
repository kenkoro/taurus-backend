package com.kenkoro.taurus.api.client.integration.auth

import com.kenkoro.taurus.api.client.annotation.Integration
import com.kenkoro.taurus.api.client.model.request.CreateUserRequest
import com.kenkoro.taurus.api.client.model.request.LoginRequest
import com.kenkoro.taurus.api.client.model.util.UserRole
import com.kenkoro.taurus.api.client.util.TestService.User.givenUser
import com.kenkoro.taurus.api.client.util.TestService.User.whenUserSignsIn
import com.kenkoro.taurus.api.client.util.TestService.configAndEnvironment
import com.kenkoro.taurus.api.client.util.TestService.thenHttpStatusCodeShouldMatch
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test

class AuthTest {
  @Test
  @Integration
  fun `should create a new user and sign in successfully`() = testApplication {
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

    val response = whenUserSignsIn(
      LoginRequest(
        subject = model.subject,
        password = model.password
      )
    )
    thenHttpStatusCodeShouldMatch(expected = HttpStatusCode.Accepted, actual = response.status)
  }

  @Test
  @Integration
  fun `should create a new user and sign in unsuccessfully`() = testApplication {
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

    val response = whenUserSignsIn(
      LoginRequest(
        subject = "test",
        password = ""
      )
    )
    thenHttpStatusCodeShouldMatch(expected = HttpStatusCode.Conflict, actual = response.status)
  }
}