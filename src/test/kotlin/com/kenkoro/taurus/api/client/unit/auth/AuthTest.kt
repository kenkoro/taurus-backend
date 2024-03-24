package com.kenkoro.taurus.api.client.unit.auth

import com.kenkoro.taurus.api.client.annotation.Unit
import com.kenkoro.taurus.api.client.util.TestService.User.whenUserSignsIn
import com.kenkoro.taurus.api.client.util.TestService.applicationConfigAndClientPlugins
import com.kenkoro.taurus.api.client.util.TestService.thenHttpStatusCodeShouldMatch
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test

class AuthTest {
  @Test
  @Unit
  fun `should respond with a bad request because of invalid request model`() = testApplication {
    applicationConfigAndClientPlugins(this)

    val response = whenUserSignsIn(BadRequest())
    thenHttpStatusCodeShouldMatch(expected = HttpStatusCode.BadRequest, actual = response.status)
  }
}