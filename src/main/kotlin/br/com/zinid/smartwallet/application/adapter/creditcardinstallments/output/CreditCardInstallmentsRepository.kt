package br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output

import org.springframework.data.jpa.repository.JpaRepository

interface CreditCardInstallmentsRepository : JpaRepository<CreditCardInstallmentsEntity, Long> {

    fun findByExpenseId(expenseId: Long): CreditCardInstallmentsEntity?

}