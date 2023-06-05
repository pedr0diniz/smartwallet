package br.com.zinid.smartwallet.application.adapter.expense.credit.output

import org.springframework.data.jpa.repository.JpaRepository

interface CreditExpenseRepository : JpaRepository<CreditExpenseEntity, Long> {

    fun findByPaymentMethodId(paymentMethodId: Long): List<CreditExpenseEntity>
}
