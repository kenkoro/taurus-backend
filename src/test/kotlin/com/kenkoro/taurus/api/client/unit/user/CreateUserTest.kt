package com.kenkoro.taurus.api.client.unit.user

import com.kenkoro.taurus.api.client.annotation.Unit
import com.kenkoro.taurus.api.client.models.request.user.Create
import com.kenkoro.taurus.api.client.models.util.UserProfile
import com.kenkoro.taurus.api.client.util.BadRequest
import com.kenkoro.taurus.api.client.util.TestService.User.givenUser
import com.kenkoro.taurus.api.client.util.TestService.applicationConfigAndClientPlugins
import com.kenkoro.taurus.api.client.util.TestService.thenHttpStatusCodeShouldMatch
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test

class CreateUserTest {
  @Test
  @Unit
  fun `should respond with a bad request because of invalid request model`() = testApplication {
    applicationConfigAndClientPlugins(this)

    val response = givenUser(BadRequest())
    thenHttpStatusCodeShouldMatch(expected = HttpStatusCode.BadRequest, actual = response.status)
  }

  @Test
  @Unit
  fun `should conflict because of invalid user credentials`() = testApplication {
    applicationConfigAndClientPlugins(this)

    val response = givenUser(
      Create(
        subject = "",
        password = "",
        image = "",
        firstName = "",
        lastName = "",
        profile = UserProfile.Others
      )
    )
    thenHttpStatusCodeShouldMatch(expected = HttpStatusCode.Conflict, actual = response.status)
  }
}