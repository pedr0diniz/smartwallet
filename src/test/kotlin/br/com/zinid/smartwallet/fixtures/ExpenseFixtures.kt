package br.com.zinid.smartwallet.fixtures

import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import java.math.BigDecimal
import java.time.LocalDate

object ExpenseFixtures {

    fun getExpense(paymentMethod: PaymentMethod) = Expense(
        id = 5L,
        content = "RK84 Mechanical Keyboard",
        date = LocalDate.now(),
        price = BigDecimal.valueOf(320.0),
        essential = false,
        monthlySubscription = false,
        paymentMethod = paymentMethod,

    )
}