package dev.chargedbyte.reaktor_summer_2022.utils

import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.sql.Connection

suspend fun <T> suspendedDatabaseQuery(block: suspend () -> T): T =
    newSuspendedTransaction(Dispatchers.IO, transactionIsolation = Connection.TRANSACTION_READ_UNCOMMITTED) { block() }
