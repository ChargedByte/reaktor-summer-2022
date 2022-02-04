package dev.chargedbyte.reaktor_summer_2022.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import dev.chargedbyte.reaktor_summer_2022.config.AppConfig
import dev.chargedbyte.reaktor_summer_2022.model.User
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

private fun Authentication.Configuration.jwt(
    name: String,
    realm: String,
    secret: String,
    audience: String,
    issuer: String,
    user: User?
) {
    jwt(name) {
        this.realm = realm
        skipWhen { secret == "idc" || user == null }
        verifier(JWT.require(Algorithm.HMAC256(secret)).withAudience(audience).withIssuer(issuer).build())
        validate { credential ->
            if (credential.payload.getClaim("username").asString() == user?.username) {
                return@validate JWTPrincipal(credential.payload)
            }
            null
        }
    }
}

private fun Routing.login(path: String, secret: String, audience: String, issuer: String, user: User?) {
    post(path) {
        val userCandidate = call.receive<User>()

        if (user != null && userCandidate == user) {
            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("username", user.username)
                .withExpiresAt(Date(Instant.now().plus(15, ChronoUnit.MINUTES).toEpochMilli()))
                .sign(Algorithm.HMAC256(secret))
            call.respond("token" to token)
            return@post
        }

        call.respond(HttpStatusCode.Unauthorized, "Invalid username or password")
    }
}

@Suppress("DuplicatedCode")
fun Application.configureSecurity(config: AppConfig) {
    val apiAudience = "${config.custom.baseUrl}/v1"
    val apiIssuer = config.custom.baseUrl.let { if (it.endsWith("/")) it else "$it/" }
    val apiSecret = config.custom.jwt.apiSecret ?: "idc"
    val apiUser = config.custom.security.apiUser

    val metricsAudience = "${config.custom.baseUrl}/metrics"
    val metricsIssuer = config.custom.baseUrl.let { if (it.endsWith("/")) it else "$it/" }
    val metricsSecret = config.custom.jwt.metricsSecret ?: "idc"
    val metricsUser = config.custom.security.metricsUser

    authentication {
        jwt("api", "Reaktor Summer 2022 - API", apiSecret, apiAudience, apiIssuer, apiUser)

        jwt("metrics", "Reaktor Summer 2022 - Metrics", metricsSecret, metricsAudience, metricsIssuer, metricsUser)
    }

    routing {
        login("/v1/login", apiSecret, apiAudience, apiIssuer, apiUser)

        login("/metrics/login", metricsSecret, metricsAudience, metricsIssuer, metricsUser)
    }
}
