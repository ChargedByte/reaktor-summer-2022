package dev.chargedbyte.reaktor_summer_2022.plugins

import dev.chargedbyte.reaktor_summer_2022.config.AppConfig
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.features.*
import io.ktor.metrics.micrometer.*
import io.ktor.response.*
import io.ktor.routing.*
import io.micrometer.prometheus.PrometheusConfig
import io.micrometer.prometheus.PrometheusMeterRegistry

fun Application.configureMonitoring(config: AppConfig) {
    if (config.custom.metrics.enable) {
        val appMicrometerRegistry = PrometheusMeterRegistry(PrometheusConfig.DEFAULT)
        install(MicrometerMetrics) {
            registry = appMicrometerRegistry
        }

        routing {
            authenticate("metrics") {
                get("/metrics/micrometer") {
                    call.respond(appMicrometerRegistry.scrape())
                }
            }
        }
    }

    install(CallLogging)
}
