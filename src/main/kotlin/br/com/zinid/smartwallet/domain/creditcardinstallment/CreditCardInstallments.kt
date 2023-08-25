package br.com.zinid.smartwallet.domain.creditcardinstallment

import br.com.zinid.smartwallet.domain.exception.NoInstallmentsException
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import br.com.zinid.smartwallet.domain.utils.DateHelper.getDateWithValidDay
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate

data class CreditCardInstallments(
    val id: Long = 0L,
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
                dueDate = getDueDate(expense.date),
                installmentValue = firstInstallmentValue,
                content = expense.content
            )
        )

        for (i in 2..numberOfMonths) {
            installments.add(
                CreditCardInstallment(
                    dueDate = getDueDate(expense.date.plusMonths(i - 1L)),
                    installmentValue = installmentValue,
                    content = expense.content
                )
            )
        }
        return installments
    }

    private fun getDueDate(currentDate: LocalDate): LocalDate {
        val dueDay = invoiceDueDayOfMonth

        if (dueDay > currentDate.dayOfMonth) {
            return getDateWithValidDay(currentDate, dueDay)
        }

        return getDateWithValidDay(currentDate.plusMonths(1), dueDay)
    }

    companion object {

        fun createFromExpenseAndCreditCard(expense: CreditExpense, creditCard: CreditCard): CreditCardInstallments {
            val numberOfInstallments =
                expense.numberOfInstallments ?: throw NoInstallmentsException(NO_INSTALLMENTS_MESSAGE)
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

        private const val NO_INSTALLMENTS_MESSAGE =
            "Cannot build installments when the amount of installments is not informed"
    }
}

data class CreditCardInstallment(
    val dueDate: LocalDate,
    val installmentValue: BigDecimal,
    val content: String
)
