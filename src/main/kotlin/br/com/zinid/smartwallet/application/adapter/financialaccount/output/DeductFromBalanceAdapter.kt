package br.com.zinid.smartwallet.application.adapter.financialaccount.output

import br.com.zinid.smartwallet.application.adapter.paymentmethod.output.PaymentMethodRepository
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.financialaccount.output.DeductFromBalanceOutputPort
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class DeductFromBalanceAdapter(
    val financialAccountRepository: FinancialAccountRepository
) : DeductFromBalanceOutputPort {
    override fun deduct(value: BigDecimal, financialAccount: FinancialAccount): Boolean {
        return financialAccount.deduct(value)
    }
}