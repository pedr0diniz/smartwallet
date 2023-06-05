package br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output

import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountEntity
import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentType
import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "debit_payment_method")
class DebitPaymentMethodEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val type: String? = null,

    @OneToOne
    @JoinColumn(name = "financial_account_id", referencedColumnName = "id", nullable = false)
    val financialAccount: FinancialAccountEntity? = null,
) {
    fun toDomain(debitExpenses: List<DebitExpense>? = null) = DebitPaymentMethod(
        id = id,
        type = PaymentType.valueOf(type!!),
        financialAccount = financialAccount?.toDomain() ?: FinancialAccount.createBlank(),
        expenses = debitExpenses
    )

    companion object {
        fun fromDomain(debitPaymentMethod: DebitPaymentMethod?) = DebitPaymentMethodEntity(
            id = debitPaymentMethod?.id,
            type = debitPaymentMethod?.type?.name,
            financialAccount = FinancialAccountEntity.fromDomain(debitPaymentMethod?.financialAccount)
        )
    }
}
