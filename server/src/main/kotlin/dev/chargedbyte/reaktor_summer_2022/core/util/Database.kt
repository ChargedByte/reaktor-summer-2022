package dev.chargedbyte.reaktor_summer_2022.core.util

import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

suspend fun <T> databaseQuery(block: suspend () -> T): T = newSuspendedTransaction { block() }
