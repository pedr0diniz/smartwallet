package br.com.zinid.smartwallet.application.config.beans.paymentmethod

import br.com.zinid.smartwallet.application.adapter.paymentmethod.FindPaymentMethodAdapter
import br.com.zinid.smartwallet.domain.paymentmethod.FindPaymentMethodInputPort
import br.com.zinid.smartwallet.domain.paymentmethod.FindPaymentMethodUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class PaymentMethodConfig {

    @Bean
    fun findPaymentMethodInputPort(
        findPaymentMethodAdapter: FindPaymentMethodAdapter
    ): FindPaymentMethodInputPort = FindPaymentMethodUseCase(findPaymentMethodAdapter)
}
