package br.com.zinid.smartwallet.domain.creditcardinstallment

import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import br.com.zinid.smartwallet.domain.utils.DateHelper.getDateWithValidDay
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

data class CreditCardInstallments(
    val id: Long? = null,
    val numberOfMonths: Int,
    val totalValue: BigDecimal,
    val firstInstallmentValue: BigDecimal,
    val installmentValue: BigDecimal,
    val invoiceDueDayOfMonth: Int,
    val installments: List<CreditCardInstallment>? = listOf(),
    val expense: CreditExpense
) {
    fun getOngoingInstallments(lastClosingDate: LocalDate): List<CreditCardInstallment> = installments
        ?.filter {
            it.dueDate > lastClosingDate
        } ?: listOf()

    fun getInstallmentsByPeriod(startDate: LocalDate, endDate: LocalDate) =
        installments?.filter {
            (it.dueDate > startDate) && (it.dueDate <= endDate)
        } ?: listOf()

    fun getOngoingInstallmentsValue(lastClosingDate: LocalDate): BigDecimal = getOngoingInstallments(lastClosingDate)
        .sumOf { it.installmentValue }

    fun buildInstallmentsList(): List<CreditCardInstallment> {
        val installments: MutableList<CreditCardInstallment> = mutableListOf()
        installments.add(
            CreditCardInstallment(
                dueDate = getClosingDate(expense.date),
                installmentValue = firstInstallmentValue,
                content = expense.content
            )
        )

        for (i in 2..numberOfMonths) {
            installments.add(
                CreditCardInstallment(
                    dueDate = getClosingDate(expense.date.plusMonths(i - 1L)),
                    installmentValue = installmentValue,
                    content = expense.content
                )
            )
        }
        return installments
    }

    private fun getClosingDate(currentDate: LocalDate): LocalDate {
        val closingDay = invoiceDueDayOfMonth

        if (closingDay > currentDate.dayOfMonth) {
            return getDateWithValidDay(currentDate, closingDay)
        }

        return getDateWithValidDay(currentDate.plusMonths(1), closingDay)
    }

    companion object {
        private const val CLOSING_TO_DUE_DATE_DELAY_IN_DAYS = 10

        fun createBlankFromNumberOfInstallments(numberOfMonths: Int) = CreditCardInstallments(
            id = 0L,
            numberOfMonths = numberOfMonths,
            totalValue = BigDecimal.ZERO,
            firstInstallmentValue = BigDecimal.ZERO,
            installmentValue = BigDecimal.ZERO,
            invoiceDueDayOfMonth = 11,
            expense = CreditExpense.createBlank()
        )

        fun createFromExpenseAndCreditCard(expense: CreditExpense, creditCard: CreditCard): CreditCardInstallments {
            val numberOfInstallments = expense.numberOfInstallments ?: throw IllegalStateException("Must have number of months")
            val installmentValue = expense.price
                .divide(
                    BigDecimal.valueOf(expense.numberOfInstallments.toLong()),
                    2,
                    RoundingMode.DOWN
                )

            val firstInstallmentValue = expense.price.minus(
                installmentValue
                    .multiply(
                        BigDecimal.valueOf(expense.numberOfInstallments.toLong())
                            .minus(BigDecimal.ONE)
                    )
            )

            val creditCardInstallments = CreditCardInstallments(
                numberOfMonths = numberOfInstallments,
                totalValue = expense.price,
                firstInstallmentValue = firstInstallmentValue,
                installmentValue = installmentValue,
                invoiceDueDayOfMonth = creditCard.invoiceDueDayOfMonth,
                expense = expense
            )
            val installmentsList = creditCardInstallments.buildInstallmentsList()

            return creditCardInstallments.copy(installments = installmentsList)
        }
    }
}

data class CreditCardInstallment(
    val dueDate: LocalDate,
    val installmentValue: BigDecimal,
    val content: String
)
