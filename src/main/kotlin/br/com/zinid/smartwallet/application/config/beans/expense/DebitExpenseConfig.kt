package br.com.zinid.smartwallet.application.config.beans.expense

import br.com.zinid.smartwallet.application.adapter.expense.debit.output.CreateDebitExpenseAdapter
import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FindFinancialAccountAdapter
import br.com.zinid.smartwallet.application.adapter.financialaccount.output.UpdateFinancialAccountAdapter
import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.FindDebitPaymentMethodAdapter
import br.com.zinid.smartwallet.domain.expense.debit.input.CreateDebitExpenseInputPort
import br.com.zinid.smartwallet.domain.expense.debit.input.CreateDebitExpenseUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DebitExpenseConfig {

    @Bean
    fun createDebitExpenseInputPort(
        findDebitPaymentMethodAdapter: FindDebitPaymentMethodAdapter,
        findFinancialAccountAdapter: FindFinancialAccountAdapter,
        updateFinancialAccountAdapter: UpdateFinancialAccountAdapter,
        createDebitExpenseAdapter: CreateDebitExpenseAdapter
    ): CreateDebitExpenseInputPort {
        return CreateDebitExpenseUseCase(
            findDebitPaymentMethodAdapter,
            findFinancialAccountAdapter,
            updateFinancialAccountAdapter,
            createDebitExpenseAdapter
        )
    }
}
