package br.com.zinid.smartwallet.application.adapter.paymentmethod.output

import br.com.zinid.smartwallet.application.adapter.expense.output.ExpenseEntity
import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountEntity
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethods
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "payment_method")
data class PaymentMethodEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val method: String? = null,

    @ManyToOne
    @JoinColumn(name = "financial_account_id", referencedColumnName = "id", nullable = false)
    val financialAccount: FinancialAccountEntity? = null,

    @OneToMany(mappedBy = "paymentMethod")
    val expenses: List<ExpenseEntity>? = listOf()
) {

    fun toDomain() = PaymentMethod(
        id = id,
        method = PaymentMethods.valueOf(method!!),
        financialAccount = financialAccount?.toDomain() ?: FinancialAccount.createBlank()
    )

    companion object {
        fun fromDomain(paymentMethod: PaymentMethod?) = PaymentMethodEntity(
            id = paymentMethod?.id,
            method = paymentMethod?.method.toString(),
            financialAccount = FinancialAccountEntity.fromDomain(paymentMethod?.financialAccount)
        )
    }
}
