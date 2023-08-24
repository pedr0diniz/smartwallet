package br.com.zinid.smartwallet.application.adapter.financialaccount.output

import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.financialaccount.output.UpdateFinancialAccountOutputPort
import org.springframework.stereotype.Service

@Service
class UpdateFinancialAccountAdapter(
    private val financialAccountRepository: FinancialAccountRepository
) : UpdateFinancialAccountOutputPort {

    override fun update(financialAccount: FinancialAccount): FinancialAccount =
        financialAccountRepository.save(FinancialAccountEntity.fromDomain(financialAccount)).toDomain()

    override fun updateByDebitExpense(debitExpense: DebitExpense): FinancialAccount =
        update(debitExpense.getFinancialAccount())
}
