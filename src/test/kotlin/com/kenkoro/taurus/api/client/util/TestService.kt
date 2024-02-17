package com.kenkoro.taurus.api.client.util

import com.kenkoro.taurus.api.client.data.remote.util.UpdateType
import com.kenkoro.taurus.api.client.model.request.CreateUserRequest
import com.kenkoro.taurus.api.client.model.request.LoginRequest
import com.kenkoro.taurus.api.client.model.response.AuthResponse
import com.kenkoro.taurus.api.client.model.util.UserRole
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import kotlin.test.assertEquals

object TestService {
  private lateinit var client: HttpClient

  private fun client(client: HttpClient) {
    TestService.client = client
  }

  internal fun applicationConfigAndClientPlugins(builder: ApplicationTestBuilder) {
    builder.environment {
      config = ApplicationConfig("application-test.conf")
    }

    client = builder.createClient {
      install(ContentNegotiation) {
        json()
      }
    }

    client(client)
  }

  internal fun thenHttpStatusCodeShouldMatch(expected: HttpStatusCode, actual: HttpStatusCode) {
    assertEquals(expected, actual)
  }

  internal fun thenHttpStatusMessageShouldMatch(expected: HttpStatusCode, actual: HttpStatusCode) {
    assertEquals(expected, actual)
  }

  object User {
    private lateinit var token: String

    internal fun token(token: String): User {
      User.token = token
      return this
    }

    internal suspend inline fun <reified T> givenUser(body: T): HttpResponse {
      return client.post("/api/create/user") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
    }

    internal suspend inline fun <reified T> whenUserSignsIn(body: T): HttpResponse {
      return client.post("/api/login") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
    }

    internal suspend fun whenGettingUser(subject: String): HttpResponse {
      return client.get("/api/user/@$subject") {
        contentType(ContentType.Application.Json)
        headers {
          append("Authorization", "Bearer $token")
        }
      }
    }

    internal suspend inline fun <reified T> whenDeletingUser(body: T): HttpResponse {
      return client.delete("/api/delete/user") {
        contentType(ContentType.Application.Json)
        setBody(body)
        headers {
          append("Authorization", "Bearer $token")
        }
      }
    }

    internal suspend inline fun <reified T> whenUpdatingUser(subject: String, data: UpdateType, body: T): HttpResponse {
      return client.put("/api/user/@$subject/edit/${data.toSql}") {
        contentType(ContentType.Application.Json)
        setBody(body)
        headers {
          append("Authorization", "Bearer $token")
        }
      }
    }

    internal suspend fun createANewTestUserThenLoginAndGetSubjectAndToken(
      builder: ApplicationTestBuilder,
      role: UserRole = UserRole.ADMIN
    ): Pair<String, String> {
      applicationConfigAndClientPlugins(builder)

      val model = CreateUserRequest(
        subject = "test",
        password = "test",
        image = "",
        firstName = "test",
        lastName = "",
        role = role
      )
      givenUser(model)

      val body = whenUserSignsIn(
        LoginRequest(
          subject = model.subject,
          password = model.password
        )
      ).body<AuthResponse>()

      return Pair(model.subject, body.token)
    }
  }
}