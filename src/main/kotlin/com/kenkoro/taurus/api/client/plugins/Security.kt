package com.kenkoro.taurus.api.client.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kenkoro.taurus.api.client.exception.EnvException
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
  val jwtAudience = environment.config.property("jwt.audience").getString()
  val jwtDomain = environment.config.property("jwt.domain").getString()
  val jwtRealm = environment.config.property("jwt.realm").getString()
  val jwtSecret = System.getenv("JWT_SECRET") ?: throw EnvException("The secret jwt key wasn't provided")

  authentication {
    jwt("jwt.auth") {
      realm = jwtRealm
      verifier(
        JWT
          .require(Algorithm.HMAC256(jwtSecret))
          .withAudience(jwtAudience)
          .withIssuer(jwtDomain)
          .build()
      )

      validate { credential ->
        if (isAudienceValid(credential, jwtAudience) && isIssuerValid(credential, jwtDomain)) {
          JWTPrincipal(credential.payload)
        } else null
      }

      challenge { _, realm ->
        call.respond(
          status = HttpStatusCode.Unauthorized,
          message = "Token is not valid or has expired. Realm: $realm"
        )
      }
    }
  }
}

fun isAudienceValid(credential: JWTCredential, audience: String): Boolean {
  return credential.payload.audience.contains(audience)
}

fun isIssuerValid(credential: JWTCredential, issuer: String): Boolean {
  return credential.payload.issuer.contains(issuer)
}