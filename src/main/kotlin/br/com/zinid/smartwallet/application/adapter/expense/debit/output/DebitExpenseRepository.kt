package br.com.zinid.smartwallet.application.adapter.expense.debit.output

import org.springframework.data.jpa.repository.JpaRepository

interface DebitExpenseRepository : JpaRepository<DebitExpenseEntity, Long> {

    fun findByPaymentMethodId(paymentMethodId: Long): List<DebitExpenseEntity>
}
