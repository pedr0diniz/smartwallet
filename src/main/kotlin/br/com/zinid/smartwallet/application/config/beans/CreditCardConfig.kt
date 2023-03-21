package br.com.zinid.smartwallet.application.config.beans

import br.com.zinid.smartwallet.application.adapter.creditcard.output.CreateCreditCardAdapter
import br.com.zinid.smartwallet.application.adapter.paymentmethod.output.FindPaymentMethodAdapter
import br.com.zinid.smartwallet.domain.creditcard.input.CreateCreditCardInputPort
import br.com.zinid.smartwallet.domain.creditcard.input.CreateCreditCardUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreditCardConfig {

    @Bean
    fun createCreditCardInputPort(
        findPaymentMethodAdapter: FindPaymentMethodAdapter,
        createCreditCardAdapter: CreateCreditCardAdapter
    ): CreateCreditCardInputPort =
        CreateCreditCardUseCase(findPaymentMethodAdapter, createCreditCardAdapter)
}
