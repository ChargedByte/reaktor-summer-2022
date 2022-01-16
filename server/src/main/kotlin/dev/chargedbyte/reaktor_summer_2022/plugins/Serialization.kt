package dev.chargedbyte.reaktor_summer_2022.plugins

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.jackson.*
import org.litote.kmongo.id.jackson.IdJacksonModule

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        jackson {
            registerModule(IdJacksonModule())
        }
    }
}
