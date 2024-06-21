package com.emm.justchill.experiences.hh.domain

import com.emm.justchill.Transactions
import com.emm.justchill.experiences.hh.data.TransactionInsert
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    suspend fun add(entity: TransactionInsert)

    fun all(): Flow<List<Transactions>>
}