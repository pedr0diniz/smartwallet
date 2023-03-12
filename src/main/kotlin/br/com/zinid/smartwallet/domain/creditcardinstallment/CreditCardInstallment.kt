package br.com.zinid.smartwallet.domain.creditcardinstallment

import br.com.zinid.smartwallet.domain.creditcard.CreditCard
import br.com.zinid.smartwallet.domain.expense.Expense
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

data class CreditCardInstallments(
    val id: Long? = null,
    val numberOfMonths: Int,
    val totalValue: BigDecimal,
    val firstInstallmentValue: BigDecimal,
    val installmentValue: BigDecimal,
    val invoiceClosingDayOfMonth: Int,
    val installments: List<CreditCardInstallment>? = listOf(),
    val expense: Expense
) {
    private fun getOngoingInstallments(lastClosingDate: LocalDate): List<CreditCardInstallment> = installments
        ?.filter {
            it.dueDate > lastClosingDate
        } ?: listOf()

    fun getOngoingInstallmentsValue(lastClosingDate: LocalDate): BigDecimal = getOngoingInstallments(lastClosingDate)
        .sumOf { it.installmentValue }

    fun getInstallmentList(): List<CreditCardInstallment> {
        val installments: MutableList<CreditCardInstallment> = mutableListOf()
//        installments.add(CreditCardInstallment(
//
//        ))
        return installments
    }

    companion object {

        fun createBlankFromNumberOfInstallments(numberOfMonths: Int) = CreditCardInstallments(
            id = 0L,
            numberOfMonths = numberOfMonths,
            totalValue = BigDecimal.ZERO,
            firstInstallmentValue = BigDecimal.ZERO,
            installmentValue = BigDecimal.ZERO,
            invoiceClosingDayOfMonth = 1,
            expense = Expense.createBlank()
        )

        fun createFromExpenseAndCreditCard(expense: Expense, creditCard: CreditCard): CreditCardInstallments {
            val numberOfInstallments = expense.creditCardInstallments!!.numberOfMonths
            val installmentValue = expense.price
                .divide(
                    BigDecimal.valueOf(expense.creditCardInstallments.numberOfMonths.toLong()),
                    2,
                    RoundingMode.DOWN
                )

            val firstInstallmentValue = expense.price.minus(
                installmentValue
                    .multiply(
                        BigDecimal.valueOf(expense.creditCardInstallments.numberOfMonths.toLong())
                            .minus(BigDecimal.ONE)
                    )
            )

            return CreditCardInstallments(
                numberOfMonths = numberOfInstallments,
                totalValue = expense.price,
                firstInstallmentValue = firstInstallmentValue,
                installmentValue = installmentValue,
                invoiceClosingDayOfMonth = creditCard.invoiceClosingDayOfMonth,
                expense = expense
            )
        }
    }
}

data class CreditCardInstallment(
    val dueDate: LocalDate,
    val installmentValue: BigDecimal
)