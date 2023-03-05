package br.com.zinid.smartwallet.application.adapter.expense.output

import org.springframework.data.jpa.repository.JpaRepository

interface ExpenseRepository : JpaRepository<ExpenseEntity, Long> {
}