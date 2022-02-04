package dev.chargedbyte.reaktor_summer_2022.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.response.*

fun Application.configureRouting() {
    install(Locations)

    install(StatusPages) {
        exception<AuthenticationException> {
            call.respond(HttpStatusCode.Unauthorized)
        }
        exception<AuthorizationException> {
            call.respond(HttpStatusCode.Forbidden)
        }

        status(HttpStatusCode.Forbidden) {
            call.respond(HttpStatusCode.Forbidden, HttpStatusCode.Forbidden.description)
        }
        status(HttpStatusCode.InternalServerError) {
            call.respond(HttpStatusCode.InternalServerError, HttpStatusCode.InternalServerError.description)
        }
        status(HttpStatusCode.NotFound) {
            call.respond(HttpStatusCode.NotFound, HttpStatusCode.NotFound.description)
        }
        status(HttpStatusCode.Unauthorized) {
            call.respond(HttpStatusCode.Unauthorized, HttpStatusCode.Unauthorized.description)
        }
    }
}

class AuthenticationException : RuntimeException()
class AuthorizationException : RuntimeException()
