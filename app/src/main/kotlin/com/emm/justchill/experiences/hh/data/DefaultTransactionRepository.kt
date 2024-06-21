package com.emm.justchill.experiences.hh.data

import com.emm.justchill.Transactions
import com.emm.justchill.experiences.hh.domain.TransactionRepository
import kotlinx.coroutines.flow.Flow

class DefaultTransactionRepository(
    private val transactionSaver: TransactionSaver,
    private val transactionRetriever: AllItemsRetriever<Transactions>,
) : TransactionRepository {

    override suspend fun add(entity: TransactionInsert) {
        transactionSaver.save(entity)
    }

    override fun all(): Flow<List<Transactions>> {
        return transactionRetriever.retrieve()
    }
}