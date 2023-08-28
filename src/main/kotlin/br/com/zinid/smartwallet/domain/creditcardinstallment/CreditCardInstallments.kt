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
    private val delayBetweenClosingAndDueDays = expense.paymentMethod.delayBetweenClosingAndDueDays

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
        val firstInstallment = CreditCardInstallment(
            dueDate = getDueDate(expense.date),
            installmentValue = firstInstallmentValue,
            content = expense.content
        )

        installments.add(firstInstallment)

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

    private fun getDueDate(expenseDate: LocalDate): LocalDate {

        // Dia de fechamento está no fim do mês e vencimento no começo do mês
        if (expenseDate.dayOfMonth + delayBetweenClosingAndDueDays > expenseDate.lengthOfMonth()) {

            // Compra foi feita entre o período de fechamento e o de vencimento
            return if (expenseDate.plusDays(delayBetweenClosingAndDueDays.toLong()).dayOfMonth > invoiceDueDayOfMonth) {
                getDateWithValidDay(expenseDate.plusMonths(2), invoiceDueDayOfMonth)
            } else {
                // Compra foi feita antes do fechamento
                getDateWithValidDay(expenseDate.plusMonths(1), invoiceDueDayOfMonth)
            }
        }

        // Dia de fechamento e vencimento estão no mesmo mês.
        // Dia da compra + delay entre vencimento e fechamento é maior que o dia do vencimento.
        return if (expenseDate.dayOfMonth + delayBetweenClosingAndDueDays > invoiceDueDayOfMonth) {
            getDateWithValidDay(expenseDate.plusMonths(1), invoiceDueDayOfMonth)
        } else {
            // Dia de fechamento e vencimento estão no mesmo mês.
            // Dia da compra + delay entre vencimento e fechamento é menor que o dia do vencimento.
            getDateWithValidDay(expenseDate, invoiceDueDayOfMonth)
        }
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
