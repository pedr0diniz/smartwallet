package br.com.zinid.smartwallet.application.config.beans

import br.com.zinid.smartwallet.application.adapter.expense.output.CreateExpenseAdapter
import br.com.zinid.smartwallet.application.adapter.paymentmethod.output.FindPaymentMethodAdapter
import br.com.zinid.smartwallet.domain.expense.input.CreateExpenseInputPort
import br.com.zinid.smartwallet.domain.expense.input.CreateExpenseUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ExpenseConfig {

    @Bean
    fun createExpenseInputPort(
        findPaymentMethodAdapter: FindPaymentMethodAdapter,
        createExpenseAdapter: CreateExpenseAdapter
    ): CreateExpenseInputPort {
        return CreateExpenseUseCase(findPaymentMethodAdapter, createExpenseAdapter)
    }
}