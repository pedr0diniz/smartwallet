package br.com.zinid.smartwallet.domain.financialaccount.output

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import java.math.BigDecimal

interface DeductFromBalanceOutputPort {

    fun deduct(value: BigDecimal, financialAccount: FinancialAccount): Boolean

}