package com.kenkoro.taurus.api.client.integration.user

import com.kenkoro.taurus.api.client.annotation.Integration
import com.kenkoro.taurus.api.client.services.util.UserUpdateType
import com.kenkoro.taurus.api.client.models.request.user.UpdateUser
import com.kenkoro.taurus.api.client.util.TestService.User.createANewTestUserThenLoginAndGetSubjectAndToken
import com.kenkoro.taurus.api.client.util.TestService.User.token
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateUserTestController {
  @Test
  @Integration
  fun `should create a new user, login and update its subject`() = testApplication {
    val (subject, token) = createANewTestUserThenLoginAndGetSubjectAndToken(this)

    val model = UpdateUser("new-test")
    val response = token(token).whenUpdatingUser(subject, UserUpdateType.Subject, model)

    assertEquals(HttpStatusCode.OK, response.status)
  }
}