package br.com.zinid.smartwallet.fixtures

import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import java.math.BigDecimal
import java.time.LocalDate

object CreditExpenseFixtures {

    fun getCreditExpenseList(creditCard: CreditCard) = listOf(
        getVacuumCleanerCreditExpense(creditCard),
        getFoodDeliveryCreditExpense(creditCard),
        getSubscriptionCreditExpense(creditCard)
    )

    fun getVacuumCleanerCreditExpense(creditCard: CreditCard): CreditExpense {
        val expense = CreditExpense(
            id = 3L,
            content = "Aspirador Robô Kabum Smart 700",
            date = LocalDate.now().withDayOfMonth(5),
            price = BigDecimal.valueOf(1399.72),
            essential = false,
            monthlySubscription = false,
            paymentMethod = creditCard,
            numberOfInstallments = 10
        )

        return expense.copy(creditCardInstallments = expense.buildInstallments())
    }

    fun getFoodDeliveryCreditExpense(creditCard: CreditCard): CreditExpense = CreditExpense(
        id = 4L,
        content = "iFood - Pizzaria Reis Magos",
        date = LocalDate.now().minusDays(3L),
        price = BigDecimal.valueOf(99.72),
        essential = false,
        monthlySubscription = false,
        paymentMethod = creditCard,
    )

    fun getSubscriptionCreditExpense(creditCard: CreditCard): CreditExpense = CreditExpense(
        id = 5L,
        content = "Netflix",
        date = LocalDate.now().withDayOfMonth(10),
        price = BigDecimal.valueOf(55.90),
        essential = false,
        monthlySubscription = true,
        paymentMethod = creditCard,
    )

    fun getRenovationsCreditExpense(creditCard: CreditCard): CreditExpense = CreditExpense(
        id = 6L,
        content = "Cor e Piso",
        date = LocalDate.now().withDayOfMonth(10),
        price = BigDecimal.valueOf(11000.00),
        essential = true,
        monthlySubscription = false,
        paymentMethod = creditCard,
    )
}
