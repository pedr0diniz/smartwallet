package br.com.zinid.smartwallet.application.config.beans.paymentmethod

import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FindFinancialAccountAdapter
import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.CreateDebitPaymentMethodAdapter
import br.com.zinid.smartwallet.domain.paymentmethod.debit.input.CreateDebitPaymentMethodInputPort
import br.com.zinid.smartwallet.domain.paymentmethod.debit.input.CreateDebitPaymentMethodUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DebitPaymentMethodConfig {

    @Bean
    fun createDebitPaymentMethodInputPort(
        findFinancialAccountAdapter: FindFinancialAccountAdapter,
        createDebitPaymentMethodAdapter: CreateDebitPaymentMethodAdapter
    ): CreateDebitPaymentMethodInputPort {
        return CreateDebitPaymentMethodUseCase(
            findFinancialAccountAdapter,
            createDebitPaymentMethodAdapter
        )
    }
}
