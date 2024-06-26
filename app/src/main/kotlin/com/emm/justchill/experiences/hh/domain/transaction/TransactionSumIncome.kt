package com.emm.justchill.experiences.hh.domain.transaction

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.math.BigDecimal

class TransactionSumIncome(private val repository: TransactionRepository) {

    operator fun invoke(): Flow<BigDecimal> {
        return repository.sumIncome().map(::fromCentsToSoles)
    }
}