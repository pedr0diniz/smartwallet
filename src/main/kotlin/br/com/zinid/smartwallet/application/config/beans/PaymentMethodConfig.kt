package br.com.zinid.smartwallet.application.config.beans

import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FindFinancialAccountAdapter
import br.com.zinid.smartwallet.application.adapter.paymentmethod.output.CreatePaymentMethodAdapter
import br.com.zinid.smartwallet.domain.paymentmethod.input.CreatePaymentMethodInputPort
import br.com.zinid.smartwallet.domain.paymentmethod.input.CreatePaymentMethodUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PaymentMethodConfig {

    @Bean
    fun createPaymentMethodInputPort(
        findFinancialAccountAdapter: FindFinancialAccountAdapter,
        createPaymentMethodAdapter: CreatePaymentMethodAdapter
    ): CreatePaymentMethodInputPort {
        return CreatePaymentMethodUseCase(findFinancialAccountAdapter, createPaymentMethodAdapter)
    }
}