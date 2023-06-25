package br.com.zinid.smartwallet.fixtures

import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import java.math.BigDecimal
import java.time.LocalDate

object CreditExpenseFixtures {

    fun getCreditExpenseList(creditCard: CreditCard) = listOf(
        getVacuumCleanerCreditExpenseWithInstallments(creditCard),
        getFoodDeliveryCreditExpense(creditCard),
        getSubscriptionCreditExpense(creditCard)
    )

    fun getVacuumCleanerCreditExpenseWithInstallments(creditCard: CreditCard): CreditExpense =
        getVacuumCleanerCreditExpense(creditCard).apply { this.process() }

    fun getVacuumCleanerCreditExpense(creditCard: CreditCard): CreditExpense = CreditExpense(
        id = 3L,
        content = "Aspirador Robô Kabum Smart 700",
        date = LocalDate.now().withDayOfMonth(5),
        price = BigDecimal.valueOf(1399.72),
        essential = false,
        monthlySubscription = false,
        paymentMethod = creditCard,
        numberOfInstallments = 10
    )

    fun getFoodDeliveryCreditExpense(creditCard: CreditCard): CreditExpense = CreditExpense(
        id = 4L,
        content = "iFood - Pizzaria Reis Magos",
        date = LocalDate.now().withDayOfMonth(15),
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
