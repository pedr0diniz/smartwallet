package br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output

import br.com.zinid.smartwallet.domain.expense.debit.output.FindDebitExpenseOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.debit.output.FindDebitPaymentMethodOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindDebitPaymentMethodAdapter(
    private val debitPaymentMethodRepository: DebitPaymentMethodRepository,
    private val findDebitExpenseAdapter: FindDebitExpenseOutputPort
) : FindDebitPaymentMethodOutputPort {
    override fun findById(debitPaymentMethodId: Long): DebitPaymentMethod? {
        val possibleDebitPaymentMethod = debitPaymentMethodRepository.findByIdOrNull(debitPaymentMethodId)

        return possibleDebitPaymentMethod?.toDomain(
            debitExpenses = getDebitExpenses(possibleDebitPaymentMethod.id!!)
        )
    }

    override fun findByFinancialAccountId(financialAccountId: Long): List<DebitPaymentMethod> =
        debitPaymentMethodRepository.findByFinancialAccountId(financialAccountId)
            .map { it.toDomain(debitExpenses = getDebitExpenses(it.id!!)) }

    private fun getDebitExpenses(id: Long) = findDebitExpenseAdapter.findByDebitPaymentMethodId(id)
}
