package dev.chargedbyte.reaktor_summer_2022.config

import dev.chargedbyte.reaktor_summer_2022.model.User
import io.ktor.config.*

class AppConfig(root: ApplicationConfig) {
    val ktor = KtorConfig(root.config("ktor"))
    val custom = CustomConfig(root.config("reaktor-summer-2022"))

    class KtorConfig(parent: ApplicationConfig) {
        val deployment = DeploymentConfig(parent.config("deployment"))

        class DeploymentConfig(parent: ApplicationConfig) {
            val host = parent.property("host").getString()
            val port = parent.property("port").getString().toInt()
        }
    }

    class CustomConfig(parent: ApplicationConfig) {
        val baseUrl = parent.property("baseUrl").getString()
        val database = DatabaseConfig(parent.config("database"))
        val jwt = JwtConfig(parent.config("jwt"))
        val metrics = MetricsConfig(parent.config("metrics"))
        val proxy = ProxyConfig(parent.config("proxy"))
        val security = SecurityConfig(parent.config("security"))

        class DatabaseConfig(parent: ApplicationConfig) {
            val url = parent.propertyOrNull("url")?.getString()
        }

        class JwtConfig(parent: ApplicationConfig) {
            val apiSecret = parent.propertyOrNull("apiSecret")?.getString()
            val metricsSecret = parent.propertyOrNull("metricsSecret")?.getString()
        }

        class MetricsConfig(parent: ApplicationConfig) {
            val disable = parent.propertyOrNull("disable")?.getString()?.toBoolean() ?: false
        }

        class ProxyConfig(parent: ApplicationConfig) {
            val installForwarded = parent.propertyOrNull("installForwarded")?.getString()?.toBoolean() ?: false
            val installXForwarded = parent.propertyOrNull("installXForwarded")?.getString()?.toBoolean() ?: false
        }

        class SecurityConfig(parent: ApplicationConfig) {
            val apiUser = parent.propertyOrNull("apiUser")?.getString()
                ?.let { User(it.substringBefore(":"), it.substringAfter(":")) }
            val metricsUser = parent.propertyOrNull("metricsUser")?.getString()
                ?.let { User(it.substringBefore(":"), it.substringAfter(":")) }
        }
    }
}
