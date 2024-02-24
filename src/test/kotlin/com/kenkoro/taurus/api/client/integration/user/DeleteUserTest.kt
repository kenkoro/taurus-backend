package com.kenkoro.taurus.api.client.integration.user

import com.kenkoro.taurus.api.client.annotation.Integration
import com.kenkoro.taurus.api.client.model.request.DeleteUser
import com.kenkoro.taurus.api.client.model.util.UserRole
import com.kenkoro.taurus.api.client.util.BadRequest
import com.kenkoro.taurus.api.client.util.TestService.User.createANewTestUserThenLoginAndGetSubjectAndToken
import com.kenkoro.taurus.api.client.util.TestService.User.token
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class DeleteUserTest {
  @Test
  @Integration
  fun `should create a new user, login and delete this user`() = testApplication {
    val (subject, token) = createANewTestUserThenLoginAndGetSubjectAndToken(this)

    val model = DeleteUser(subject)
    val response = token(token).whenDeletingUser(model)

    assertEquals(HttpStatusCode.OK, response.status)
  }

  @Test
  @Integration
  fun `should create a new user, login, but it won't delete this user because of role authority`() = testApplication {
    val (subject, token) = createANewTestUserThenLoginAndGetSubjectAndToken(builder = this, role = UserRole.OTHERS)

    val model = DeleteUser(subject)
    val response = token(token).whenDeletingUser(model)

    assertEquals(HttpStatusCode.Conflict, response.status)
  }

  @Test
  @Integration
  fun `should create a new user, login, but respond with a bad request`() = testApplication {
    val (_, token) = createANewTestUserThenLoginAndGetSubjectAndToken(this)

    val response = token(token).whenDeletingUser(BadRequest())

    assertEquals(HttpStatusCode.BadRequest, response.status)
  }
}