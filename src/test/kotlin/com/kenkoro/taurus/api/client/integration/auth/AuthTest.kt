package com.kenkoro.taurus.api.client.integration.auth

import com.kenkoro.taurus.api.client.annotation.Integration
import com.kenkoro.taurus.api.client.models.request.user.Create
import com.kenkoro.taurus.api.client.models.request.login.LoginRequest
import com.kenkoro.taurus.api.client.models.util.UserProfile
import com.kenkoro.taurus.api.client.util.TestService.User.givenUser
import com.kenkoro.taurus.api.client.util.TestService.User.whenUserSignsIn
import com.kenkoro.taurus.api.client.util.TestService.applicationConfigAndClientPlugins
import com.kenkoro.taurus.api.client.util.TestService.thenHttpStatusCodeShouldMatch
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test

class AuthTest {
  @Test
  @Integration
  fun `should create a new user and sign in successfully`() = testApplication {
    applicationConfigAndClientPlugins(this)

    val model = Create(
      subject = "test",
      password = "test",
      image = "",
      firstName = "test",
      lastName = "",
      profile = UserProfile.Admin
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
    applicationConfigAndClientPlugins(this)

    val model = Create(
      subject = "test",
      password = "test",
      image = "",
      firstName = "test",
      lastName = "",
      profile = UserProfile.Admin
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