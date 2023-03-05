package br.com.zinid.smartwallet.domain.expense.input

import br.com.zinid.smartwallet.application.adapter.creditcard.output.FindCreditCardAdapter
import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.output.CreateExpenseOutputPort
import br.com.zinid.smartwallet.domain.financialaccount.output.CreateFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.financialaccount.output.DeductFromBalanceOutputPort
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.output.FindPaymentMethodOutputPort

class CreateExpenseUseCase(
    private val findPaymentMethodAdapter: FindPaymentMethodOutputPort,
    private val findFinancialAccountAdapter: FindFinancialAccountOutputPort,
    private val deductFromBalanceAdapter: DeductFromBalanceOutputPort,
    private val createFinancialAccountAdapter: CreateFinancialAccountOutputPort,
    private val createExpenseAdapter: CreateExpenseOutputPort
) : CreateExpenseInputPort {
    override fun execute(expense: Expense): Long? {
        val possiblePaymentMethod = findPaymentMethodAdapter.findById(expense.paymentMethod?.id!!) ?: return null
        val possibleFinancialAccount = findFinancialAccountAdapter.findById(possiblePaymentMethod.financialAccount?.id!!)
            ?: return null

        if (!expense.isCreditCardPurchase() && deductFromBalanceAdapter.deduct(expense.price!!, possibleFinancialAccount)) {
            createFinancialAccountAdapter.create(possibleFinancialAccount)
            return createExpenseAdapter.create(expense.copy(paymentMethod = possiblePaymentMethod))
        }

        return null
    }
}