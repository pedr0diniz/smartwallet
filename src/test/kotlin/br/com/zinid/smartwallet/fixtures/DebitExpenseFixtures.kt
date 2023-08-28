package br.com.zinid.smartwallet.fixtures

import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod
import java.math.BigDecimal
import java.time.LocalDate

object DebitExpenseFixtures {

    fun getDebitExpenseList(debitPaymentMethod: DebitPaymentMethod) = listOf(
        getTelecomBillDebitExpense(debitPaymentMethod),
        getWaterBillDebitExpense(debitPaymentMethod),
        getElectricalPowerBillDebitExpense(debitPaymentMethod)
    )

    fun getTelecomBillDebitExpense(debitPaymentMethod: DebitPaymentMethod) = DebitExpense(
        id = 1L,
        content = "NET/Claro - Internet e Telefone",
        date = LocalDate.now().withDayOfMonth(5),
        price = BigDecimal.valueOf(200.00),
        essential = true,
        monthlySubscription = true,
        paymentMethod = debitPaymentMethod
    )

    fun getWaterBillDebitExpense(debitPaymentMethod: DebitPaymentMethod) = DebitExpense(
        id = 2L,
        content = "CAERN",
        date = LocalDate.now().withDayOfMonth(18),
        price = BigDecimal.valueOf(130.00),
        essential = true,
        monthlySubscription = true,
        paymentMethod = debitPaymentMethod
    )

    fun getElectricalPowerBillDebitExpense(debitPaymentMethod: DebitPaymentMethod) = DebitExpense(
        id = 3L,
        content = "Neoenergia Cosern",
        date = LocalDate.now().withDayOfMonth(25),
        price = BigDecimal.valueOf(250.00),
        essential = true,
        monthlySubscription = true,
        paymentMethod = debitPaymentMethod
    )

    fun getExpenseWithBlankPaymentMethod(id: Long) = DebitExpense(
        id = 4L,
        content = "Detran - IPVA",
        date = LocalDate.now(),
        price = BigDecimal.valueOf(227.79),
        essential = true,
        monthlySubscription = false,
        paymentMethod = DebitPaymentMethod.createBlankFromId(id),
        tag = "IPVA"
    )
}
