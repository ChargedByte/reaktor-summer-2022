package dev.chargedbyte.reaktor_summer_2022.config

import io.ktor.config.*

class AppConfig(root: ApplicationConfig) {
    val ktor = KtorConfig(root.config("ktor"))
    val custom = CustomConfig(root.config("reaktor-summer-2022"))

    class KtorConfig(parent: ApplicationConfig) {
        val deployment = DeploymentConfig(parent.config("deployment"))

        class DeploymentConfig(parent: ApplicationConfig) {
            val host = parent.propertyOrNull("host")?.getString()
            val port = parent.property("port").getString().toInt()
        }
    }

    class CustomConfig(parent: ApplicationConfig) {
        val database = DatabaseConfig(parent.config("database"))
        val security = SecurityConfig(parent.config("security"))

        class DatabaseConfig(parent: ApplicationConfig) {
            val url = parent.propertyOrNull("url")?.getString()
        }

        class SecurityConfig(parent: ApplicationConfig) {
            val users = parent.propertyOrNull("users")?.getList()?.map { it.split(":") }?.associate { it[0] to it[1] }
                ?: emptyMap()
        }
    }
}
