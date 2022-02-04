package dev.chargedbyte.reaktor_summer_2022.plugins

import dev.chargedbyte.reaktor_summer_2022.config.AppConfig
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.util.*

fun Application.configureSecurity(config: AppConfig) {
    authentication {
        val digestFunction = getDigestFunction("SHA-256") { "reaktor-summer-2022-${it.length}" }

        val hashedUserTable = UserHashedTableAuth(
            table = config.custom.security.users.mapValues { digestFunction(it.value) },
            digester = digestFunction
        )

        basic {
            realm = "Reaktor Summer 2022"
            validate { credentials ->
                hashedUserTable.authenticate(credentials)
            }
        }
    }
}
