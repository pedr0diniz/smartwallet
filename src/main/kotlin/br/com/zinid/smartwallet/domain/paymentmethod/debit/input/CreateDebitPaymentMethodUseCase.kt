package br.com.zinid.smartwallet.domain.paymentmethod.debit.input

import br.com.zinid.smartwallet.domain.exception.DomainClasses.FINANCIAL_ACCOUNT
import br.com.zinid.smartwallet.domain.exception.NotFoundException
import br.com.zinid.smartwallet.domain.financialaccount.output.FindFinancialAccountOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.debit.output.CreateDebitPaymentMethodOutputPort

class CreateDebitPaymentMethodUseCase(
    private val findFinancialAccountAdapter: FindFinancialAccountOutputPort,
    private val createDebitPaymentMethodAdapter: CreateDebitPaymentMethodOutputPort
) : CreateDebitPaymentMethodInputPort {

    override fun execute(debitPaymentMethod: DebitPaymentMethod): DebitPaymentMethod {
        val financialAccountId = debitPaymentMethod.financialAccount.id!!
        val possibleFinancialAccount = findFinancialAccountAdapter.findById(financialAccountId)
            ?: throw NotFoundException.buildFrom(FINANCIAL_ACCOUNT, "id", financialAccountId)

        return createDebitPaymentMethodAdapter.create(debitPaymentMethod.copy(financialAccount = possibleFinancialAccount))
    }
}
