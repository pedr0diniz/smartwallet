package br.com.zinid.smartwallet.application.config.beans

import br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output.CreateCreditCardInstallmentsAdapter
import br.com.zinid.smartwallet.application.adapter.expense.output.FindExpenseAdapter
import br.com.zinid.smartwallet.domain.creditcardinstallment.input.CreateCreditCardInstallmentsInputPort
import br.com.zinid.smartwallet.domain.creditcardinstallment.input.CreateCreditCardInstallmentsUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreditCardInstallmentsConfig {

    @Bean
    fun createCreditCardInstallmentsInputPort(
        findExpenseAdapter: FindExpenseAdapter,
        createCreditCardInstallmentsAdapter: CreateCreditCardInstallmentsAdapter
    ): CreateCreditCardInstallmentsInputPort {
        return CreateCreditCardInstallmentsUseCase(findExpenseAdapter, createCreditCardInstallmentsAdapter)
    }
}