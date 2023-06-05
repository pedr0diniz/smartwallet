package br.com.zinid.smartwallet.application.config.beans.expense

import br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output.CreateCreditCardInstallmentsAdapter
import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreateCreditExpenseAdapter
import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.FindCreditCardAdapter
import br.com.zinid.smartwallet.domain.expense.credit.input.CreateCreditExpenseInputPort
import br.com.zinid.smartwallet.domain.expense.credit.input.CreateCreditExpenseUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreditExpenseConfig {

    @Bean
    fun createCreditExpenseInputPort(
        findCreditCardAdapter: FindCreditCardAdapter,
        createCreditCardInstallmentsAdapter: CreateCreditCardInstallmentsAdapter,
        createCreditExpenseAdapter: CreateCreditExpenseAdapter
    ): CreateCreditExpenseInputPort {
        return CreateCreditExpenseUseCase(
            findCreditCardAdapter,
            createCreditCardInstallmentsAdapter,
            createCreditExpenseAdapter
        )
    }
}
