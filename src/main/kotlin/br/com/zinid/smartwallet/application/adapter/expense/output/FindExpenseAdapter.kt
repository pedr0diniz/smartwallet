package br.com.zinid.smartwallet.application.adapter.expense.output

import br.com.zinid.smartwallet.domain.creditcardinstallment.output.FindCreditCardInstallmentsOutputPort
import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.output.FindExpenseOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindExpenseAdapter(
    val expenseRepository: ExpenseRepository,
    val findCreditCardInstallmentsAdapter: FindCreditCardInstallmentsOutputPort
) : FindExpenseOutputPort {

    override fun findById(id: Long): Expense? {
        val possibleExpense = expenseRepository.findByIdOrNull(id)?.toDomain()

        return possibleExpense?.copy(
            creditCardInstallments = getCreditCardInstallments(id)
        )
    }

    override fun findByPaymentMethodId(paymentMethodId: Long): List<Expense> {
        return expenseRepository.findByPaymentMethodId(paymentMethodId).map {
            it.toDomain().copy(
                creditCardInstallments = getCreditCardInstallments(it.id!!)
            )
        }
    }

    private fun getCreditCardInstallments(id: Long) = findCreditCardInstallmentsAdapter.findByExpenseId(id)
}
