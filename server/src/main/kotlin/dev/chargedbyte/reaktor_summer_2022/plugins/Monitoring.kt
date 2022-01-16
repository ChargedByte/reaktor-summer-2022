package dev.chargedbyte.reaktor_summer_2022.plugins

import io.ktor.application.*
import io.ktor.features.*

fun Application.configureMonitoring() {
    install(CallLogging)
}
