package com.kenkoro.taurus.api.client.util

import io.ktor.client.*
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

  internal fun configAndEnvironment(builder: ApplicationTestBuilder) {
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

    internal suspend inline fun <reified T> whenGettingUser(subject: String): HttpResponse {
      return client.get("/api/user/@$subject") {
        contentType(ContentType.Application.Json)
      }
    }
  }
}