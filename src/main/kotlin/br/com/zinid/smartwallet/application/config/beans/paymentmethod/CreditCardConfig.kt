package br.com.zinid.smartwallet.application.config.beans.paymentmethod

import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FindFinancialAccountAdapter
import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.CreateCreditCardAdapter
import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.FindCreditCardAdapter
import br.com.zinid.smartwallet.domain.paymentmethod.credit.input.CreateCreditCardInputPort
import br.com.zinid.smartwallet.domain.paymentmethod.credit.input.CreateCreditCardUseCase
import br.com.zinid.smartwallet.domain.paymentmethod.credit.input.FindCreditCardInputPort
import br.com.zinid.smartwallet.domain.paymentmethod.credit.input.FindCreditCardUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreditCardConfig {

    @Bean
    fun createCreditCardInputPort(
        findFinancialAccountAdapter: FindFinancialAccountAdapter,
        createCreditCardAdapter: CreateCreditCardAdapter
    ): CreateCreditCardInputPort {
        return CreateCreditCardUseCase(
            findFinancialAccountAdapter,
            createCreditCardAdapter
        )
    }

    @Bean
    fun findCreditCardInputPort(
        findCreditCardAdapter: FindCreditCardAdapter
    ): FindCreditCardInputPort = FindCreditCardUseCase(findCreditCardAdapter)
}
