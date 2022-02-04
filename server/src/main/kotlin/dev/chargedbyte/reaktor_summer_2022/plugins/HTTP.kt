package dev.chargedbyte.reaktor_summer_2022.plugins

import dev.chargedbyte.reaktor_summer_2022.config.AppConfig
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*

fun Application.configureHTTP(config: AppConfig) {
    install(DefaultHeaders) {
        header(HttpHeaders.Server, "Ktor/1.6.7")
    }

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        //allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(Compression) {
        gzip {
            priority = 1.0
        }
        deflate {
            priority = 10.0
            minimumSize(1024)
        }
    }

    if (config.custom.proxy.installForwarded)
        install(ForwardedHeaderSupport)

    if (config.custom.proxy.installXForwarded)
        install(XForwardedHeaderSupport)
}
