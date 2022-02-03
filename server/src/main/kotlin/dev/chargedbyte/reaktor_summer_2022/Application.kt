package dev.chargedbyte.reaktor_summer_2022

import com.google.inject.Guice
import dev.chargedbyte.reaktor_summer_2022.config.MainModule
import dev.chargedbyte.reaktor_summer_2022.plugins.configureHTTP
import dev.chargedbyte.reaktor_summer_2022.plugins.configureMonitoring
import dev.chargedbyte.reaktor_summer_2022.plugins.configureRouting
import dev.chargedbyte.reaktor_summer_2022.plugins.configureSerialization
import io.ktor.application.*
import io.ktor.server.netty.*

fun Application.module() {
    configureRouting()
    configureSerialization()
    configureMonitoring()
    configureHTTP()

    Guice.createInjector(MainModule(this))
}

fun main(args: Array<String>) = EngineMain.main(args)
