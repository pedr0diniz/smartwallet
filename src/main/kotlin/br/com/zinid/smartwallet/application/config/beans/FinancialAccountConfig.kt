package br.com.zinid.smartwallet.application.config.beans

import br.com.zinid.smartwallet.application.adapter.financialaccount.output.CreateFinancialAccountAdapter
import br.com.zinid.smartwallet.application.adapter.user.output.FindUserAdapter
import br.com.zinid.smartwallet.domain.financialaccount.input.CreateFinancialAccountInputPort
import br.com.zinid.smartwallet.domain.financialaccount.input.CreateFinancialAccountUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FinancialAccountConfig {

    @Bean
    fun createFinancialAccountInputPort(
        findUserAdapter: FindUserAdapter,
        createFinancialAccountAdapter: CreateFinancialAccountAdapter
    ): CreateFinancialAccountInputPort =
        CreateFinancialAccountUseCase(findUserAdapter, createFinancialAccountAdapter)
}
