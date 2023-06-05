package br.com.zinid.smartwallet.application.adapter.expense.debit.output

import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
import br.com.zinid.smartwallet.domain.expense.debit.output.FindDebitExpenseOutputPort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class FindDebitExpenseAdapter(
    private val debitExpenseRepository: DebitExpenseRepository
) : FindDebitExpenseOutputPort {

    override fun findById(id: Long): DebitExpense? =
        debitExpenseRepository.findByIdOrNull(id)?.toDomain()

    override fun findByDebitPaymentMethodId(debitPaymentMethodId: Long): List<DebitExpense> =
        debitExpenseRepository.findByPaymentMethodId(debitPaymentMethodId)
            .map { it.toDomain() }
}
