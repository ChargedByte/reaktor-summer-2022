package dev.chargedbyte.reaktor_summer_2022.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*

fun Application.configureHTTP() {
    install(DefaultHeaders) {
        header(HttpHeaders.Server, "Ktor/1.6.7")
    }
}
