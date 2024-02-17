package com.kenkoro.taurus.api.client.integration.user

import com.kenkoro.taurus.api.client.annotation.Integration
import com.kenkoro.taurus.api.client.data.remote.util.UpdateType
import com.kenkoro.taurus.api.client.model.request.UpdateUserColumnRequest
import com.kenkoro.taurus.api.client.util.TestService.User.createANewTestUserThenLoginAndGetSubjectAndToken
import com.kenkoro.taurus.api.client.util.TestService.User.token
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateUserTest {
  @Test
  @Integration
  fun `should create a new user, login and update its subject`() = testApplication {
    val (subject, token) = createANewTestUserThenLoginAndGetSubjectAndToken(this)

    val model = UpdateUserColumnRequest("new-test")
    val response = token(token).whenUpdatingUser(subject, UpdateType.SUBJECT, model)

    assertEquals(HttpStatusCode.OK, response.status)
  }
}