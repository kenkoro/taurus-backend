package com.kenkoro.taurus.api.client.integration.user

import com.kenkoro.taurus.api.client.annotation.Integration
import com.kenkoro.taurus.api.client.models.enums.UserProfile
import com.kenkoro.taurus.api.client.util.TestService.User.createANewTestUserThenLoginAndGetSubjectAndToken
import com.kenkoro.taurus.api.client.util.TestService.User.token
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteUserTestController {
  @Test
  @Integration
  fun `should create a new user, login and delete this user`() = testApplication {
    val (subject, token) = createANewTestUserThenLoginAndGetSubjectAndToken(this)

    val response = token(token).whenDeletingUser(subject)

    assertEquals(HttpStatusCode.OK, response.status)
  }

  @Test
  @Integration
  fun `should create a new user, login, but it won't delete this user because of role authority`() = testApplication {
    val (subject, token) = createANewTestUserThenLoginAndGetSubjectAndToken(builder = this, role = UserProfile.Others)

    val response = token(token).whenDeletingUser(subject)

    assertEquals(HttpStatusCode.Conflict, response.status)
  }
}