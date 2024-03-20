package com.kenkoro.taurus.api.client.core.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.kenkoro.taurus.api.client.core.security.token.JwtTokenConfigService
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*

fun Application.configureSecurity() {
  val config = JwtTokenConfigService.config()

  authentication {
    jwt(config.authName) {
      realm = config.realm
      verifier(
        JWT
          .require(Algorithm.HMAC256(config.secret))
          .withAudience(config.audience)
          .withIssuer(config.domain)
          .build()
      )

      validate { credential ->
        if (isAudienceValid(credential, config.audience) && isIssuerValid(credential, config.domain)) {
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