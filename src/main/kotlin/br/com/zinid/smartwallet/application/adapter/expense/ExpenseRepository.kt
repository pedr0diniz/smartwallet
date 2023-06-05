package br.com.zinid.smartwallet.application.adapter.expense

import org.springframework.data.jpa.repository.JpaRepository

interface ExpenseRepository : JpaRepository<ExpenseEntity, Long> {

    fun findByCreditExpenseId(creditExpenseId: Long): ExpenseEntity?

    fun findByDebitExpenseId(debitExpenseId: Long): ExpenseEntity?
}
