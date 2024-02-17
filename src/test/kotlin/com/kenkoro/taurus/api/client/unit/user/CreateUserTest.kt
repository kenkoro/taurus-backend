package com.kenkoro.taurus.api.client.unit.user

import com.kenkoro.taurus.api.client.annotation.Unit
import com.kenkoro.taurus.api.client.model.request.CreateUserRequest
import com.kenkoro.taurus.api.client.model.util.UserRole
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
      CreateUserRequest(
        subject = "",
        password = "",
        image = "",
        firstName = "",
        lastName = "",
        role = UserRole.OTHERS
      )
    )
    thenHttpStatusCodeShouldMatch(expected = HttpStatusCode.Conflict, actual = response.status)
  }
}