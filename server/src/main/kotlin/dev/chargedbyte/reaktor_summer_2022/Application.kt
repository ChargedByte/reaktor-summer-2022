package dev.chargedbyte.reaktor_summer_2022

import com.google.inject.Guice
import dev.chargedbyte.reaktor_summer_2022.config.AppConfig
import dev.chargedbyte.reaktor_summer_2022.config.MainModule
import dev.chargedbyte.reaktor_summer_2022.plugins.*
import io.ktor.application.*
import io.ktor.server.netty.*

fun Application.module() {
    val config = AppConfig(this.environment.config)

    configureRouting()
    configureSecurity(config)
    configureSerialization()
    configureMonitoring()
    configureHTTP()

    Guice.createInjector(MainModule(this, config))
}

fun main(args: Array<String>) = EngineMain.main(args)
