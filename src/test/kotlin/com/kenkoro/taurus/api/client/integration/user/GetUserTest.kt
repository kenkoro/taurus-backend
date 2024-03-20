package com.kenkoro.taurus.api.client.integration.user

import com.kenkoro.taurus.api.client.annotation.Integration
import com.kenkoro.taurus.api.client.models.request.user.Get
import com.kenkoro.taurus.api.client.util.TestService.User.createANewTestUserThenLoginAndGetSubjectAndToken
import com.kenkoro.taurus.api.client.util.TestService.User.token
import io.ktor.client.call.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class GetUserTest {
  @Test
  @Integration
  fun `should create a new user, login and get this user`() = testApplication {
    val (subject, token) = createANewTestUserThenLoginAndGetSubjectAndToken(this)

    val response = token(token).whenGettingUser(subject)
    val savedUserIntoDb = response.body<Get>()

    assertEquals(HttpStatusCode.OK, response.status)
    assertEquals(subject, savedUserIntoDb.subject)
  }

}