package br.com.zinid.smartwallet.application.adapter.expense

import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.FindExpenseOutputPort
import br.com.zinid.smartwallet.domain.expense.credit.output.FindCreditExpenseOutputPort
import br.com.zinid.smartwallet.domain.expense.debit.output.FindDebitExpenseOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindExpenseAdapter(
    private val findCreditExpenseAdapter: FindCreditExpenseOutputPort,
    private val findDebitExpenseAdapter: FindDebitExpenseOutputPort,
    private val expenseRepository: ExpenseRepository
) : FindExpenseOutputPort {
    override fun findById(id: Long): Expense? =
        expenseRepository.findByIdOrNull(id)?.toDomain()

    override fun findByCreditExpenseId(creditExpenseId: Long): Expense? =
        findCreditExpenseAdapter.findById(creditExpenseId)

    override fun findByDebitExpenseId(debitExpenseId: Long): Expense? =
        findDebitExpenseAdapter.findById(debitExpenseId)

    override fun findByPaymentMethodId(paymentMethodId: Long): List<Expense> {
        val possibleCreditExpenses = findCreditExpenseAdapter.findByCreditCardId(paymentMethodId)

        if (possibleCreditExpenses.isEmpty()) {
            return findDebitExpenseAdapter.findByDebitPaymentMethodId(paymentMethodId)
        }

        return possibleCreditExpenses
    }
}
