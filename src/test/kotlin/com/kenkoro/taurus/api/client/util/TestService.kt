package com.kenkoro.taurus.api.client.util

import com.kenkoro.taurus.api.client.models.request.login.LoginRequest
import com.kenkoro.taurus.api.client.models.request.user.CreateUser
import com.kenkoro.taurus.api.client.models.response.login.LoginResponse
import com.kenkoro.taurus.api.client.models.enums.UserProfile
import com.kenkoro.taurus.api.client.services.util.UserUpdateType
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

  object User {
    private lateinit var token: String

    internal fun token(token: String): User {
      User.token = token
      return this
    }

    internal suspend inline fun <reified T> givenUser(body: T): HttpResponse {
      return client.post("/create/user") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
    }

    internal suspend inline fun <reified T> whenUserSignsIn(body: T): HttpResponse {
      return client.post("/login") {
        contentType(ContentType.Application.Json)
        setBody(body)
      }
    }

    internal suspend fun whenGettingUser(subject: String): HttpResponse {
      return client.get("/user/@$subject") {
        contentType(ContentType.Application.Json)
        headers {
          append("Authorization", "Bearer $token")
        }
      }
    }

    internal suspend inline fun whenDeletingUser(subject: String): HttpResponse {
      return client.delete("/delete/user/@$subject") {
        contentType(ContentType.Application.Json)
        headers {
          append("Authorization", "Bearer $token")
        }
      }
    }

    internal suspend inline fun <reified T> whenUpdatingUser(
      subject: String,
      data: UserUpdateType,
      body: T
    ): HttpResponse {
      return client.put("/user/@$subject/edit/${data.toSql}") {
        contentType(ContentType.Application.Json)
        setBody(body)
        headers {
          append("Authorization", "Bearer $token")
        }
      }
    }

    internal suspend fun createANewTestUserThenLoginAndGetSubjectAndToken(
      builder: ApplicationTestBuilder,
      role: UserProfile = UserProfile.Admin
    ): Pair<String, String> {
      applicationConfigAndClientPlugins(builder)

      val model = CreateUser(
        subject = "test",
        password = "test",
        image = "",
        firstName = "test",
        lastName = "",
        email = "",
        profile = role
      )
      givenUser(model)

      val body = whenUserSignsIn(
        LoginRequest(
          subject = model.subject,
          password = model.password
        )
      ).body<LoginResponse>()

      return Pair(model.subject, body.token)
    }
  }
}