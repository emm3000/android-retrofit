package com.emm.justchill.experiences.hh.data.transaction

import android.util.Log
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import com.emm.justchill.TransactionQueries
import com.emm.justchill.Transactions
import com.emm.justchill.core.DispatchersProvider
import com.emm.justchill.experiences.hh.data.AllTransactionsRetriever
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext

class TransactionDiskDataSource(
    dispatchersProvider: DispatchersProvider,
    private val transactionQueries: TransactionQueries,
) : TransactionSaver,
    AllTransactionsRetriever,
    TransactionCalculator,
    DispatchersProvider by dispatchersProvider {

    override suspend fun save(entity: TransactionInsert) = withContext(ioDispatcher) {
        checkNotNull(entity.id)
        transactionQueries.addTransaction(
            transactionId = entity.id,
            type = entity.type,
            amount = entity.amount,
            description = entity.description,
            date = entity.date,
        )
    }

    override fun retrieve(): Flow<List<Transactions>> {
        return transactionQueries.retrieveAll().asFlow().mapToList(ioDispatcher)
    }

    override fun retrieveByDateRange(
        startDateMillis: Long,
        endDateMillis: Long
    ): Flow<List<Transactions>> {
        return transactionQueries
            .selectTransactionsByDateRange(startDateMillis, endDateMillis)
            .asFlow()
            .mapToList(ioDispatcher)
    }

    override fun sumSpend(): Flow<Long> {
        return transactionQueries.sumAllSpendAmounts()
            .asFlow()
            .mapToOneOrNull(ioDispatcher)
            .map { it?.totalIncome ?: 0L }
    }

    override fun sumIncome(): Flow<Long> {
        return transactionQueries.sumAllIncomeAmounts()
            .asFlow()
            .mapToOneOrNull(ioDispatcher)
            .onEach { Log.e("aea", it.toString()) }
            .map { it?.totalIncome ?: 0L }
    }

    override fun difference(): Flow<Long> {
        return transactionQueries.difference()
            .asFlow()
            .mapToOneOrNull(ioDispatcher)
            .map { it ?: 0L }
    }
}