package com.kenkoro.taurus.api.client.integration.auth

import com.kenkoro.taurus.api.client.integration.auth.annotation.Integration
import com.kenkoro.taurus.api.client.model.request.CreateUserRequest
import com.kenkoro.taurus.api.client.model.request.LoginRequest
import com.kenkoro.taurus.api.client.model.util.UserRole
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlin.test.Ignore
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthTest {
  @Test
  @Integration
  @Ignore
  fun `should create a new user and sign in successfully`() = testApplication {
    environment {
      config = ApplicationConfig("application-test.conf")
    }

    val client = createClient {
      install(ContentNegotiation) {
        json()
      }
    }

    givenTestUser(client)
    val response = whenUserSignsIn(client)
    thenSignInShouldBeSuccessful(response)
  }

  private suspend fun givenTestUser(client: HttpClient) {
    val response = client.post("/api/create/user") {
      contentType(ContentType.Application.Json)
      setBody(
        CreateUserRequest(
          subject = "test",
          password = "test",
          image = "",
          firstName = "test",
          lastName = "",
          role = UserRole.ADMIN
        )
      )
    }

    assertEquals(HttpStatusCode.Created, response.status)
  }

  private suspend fun whenUserSignsIn(client: HttpClient): HttpResponse {
    return client.post("/api/login") {
      contentType(ContentType.Application.Json)
      setBody(
        LoginRequest(
          subject = "test",
          password = "test"
        )
      )
    }
  }

  private fun thenSignInShouldBeSuccessful(response: HttpResponse) {
    assertEquals(HttpStatusCode.Accepted, response.status)
  }
}