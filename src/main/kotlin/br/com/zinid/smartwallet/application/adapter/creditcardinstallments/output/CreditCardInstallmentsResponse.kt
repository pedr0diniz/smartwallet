package br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output

import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallment
import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import java.math.BigDecimal
import java.time.LocalDate

data class CreditCardInstallmentsResponse(
    val id: Long? = null,
    val numberOfMonths: Int,
    val totalValue: BigDecimal,
    val firstInstallmentValue: BigDecimal,
    val installmentValue: BigDecimal,
    val invoiceClosingDayOfMonth: Int,
    val installments: List<CreditCardInstallmentResponse>? = listOf()
) {
    companion object {
        fun fromDomain(creditCardInstallments: CreditCardInstallments) = CreditCardInstallmentsResponse(
            id = creditCardInstallments.id,
            numberOfMonths = creditCardInstallments.numberOfMonths,
            totalValue = creditCardInstallments.totalValue,
            firstInstallmentValue = creditCardInstallments.firstInstallmentValue,
            installmentValue = creditCardInstallments.installmentValue,
            invoiceClosingDayOfMonth = creditCardInstallments.invoiceClosingDayOfMonth,
            installments = creditCardInstallments.installments?.map { CreditCardInstallmentResponse.fromDomain(it) }
        )
    }
}

data class CreditCardInstallmentResponse(
    val dueDate: LocalDate,
    val installmentValue: BigDecimal
) {
    companion object {
        fun fromDomain(creditCardInstallment: CreditCardInstallment) = CreditCardInstallmentResponse(
            dueDate = creditCardInstallment.dueDate,
            installmentValue = creditCardInstallment.installmentValue
        )
    }
}
