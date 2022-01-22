package dev.chargedbyte.reaktor_summer_2022.utils

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

suspend fun <T> suspendedDatabaseQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }

fun <T> databaseQuery(block: () -> T): T = transaction { block() }
