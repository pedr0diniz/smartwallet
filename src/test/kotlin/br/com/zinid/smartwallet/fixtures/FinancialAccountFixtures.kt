package br.com.zinid.smartwallet.fixtures

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.financialaccount.Institutions
import br.com.zinid.smartwallet.domain.user.User
import java.math.BigDecimal

object FinancialAccountFixtures {

    fun mockFinancialAccount(user: User) = FinancialAccount(
        id = 1L,
        institution = Institutions.ITI.name,
        balance = BigDecimal.ZERO,
        overdraft = BigDecimal.ZERO,
        user = user
    )
}
