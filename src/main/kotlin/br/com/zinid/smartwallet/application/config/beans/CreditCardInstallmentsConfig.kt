package br.com.zinid.smartwallet.application.config.beans

import br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output.FindCreditCardInstallmentsAdapter
import br.com.zinid.smartwallet.application.adapter.expense.credit.output.FindCreditExpenseAdapter
import br.com.zinid.smartwallet.domain.creditcardinstallment.input.FindCreditCardInstallmentsInputPort
import br.com.zinid.smartwallet.domain.creditcardinstallment.input.FindCreditCardInstallmentsUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class CreditCardInstallmentsConfig {

    @Bean
    fun findCreditCardInstallmentsInputPort(
        findCreditExpenseAdapter: FindCreditExpenseAdapter,
        findCreditCardInstallmentsAdapter: FindCreditCardInstallmentsAdapter
    ): FindCreditCardInstallmentsInputPort =
        FindCreditCardInstallmentsUseCase(findCreditExpenseAdapter, findCreditCardInstallmentsAdapter)
}
