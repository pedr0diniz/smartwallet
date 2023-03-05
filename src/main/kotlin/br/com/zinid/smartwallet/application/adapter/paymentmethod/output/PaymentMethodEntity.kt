package br.com.zinid.smartwallet.application.adapter.paymentmethod.output

import br.com.zinid.smartwallet.application.adapter.creditcard.output.CreditCardEntity
import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountEntity
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import jakarta.persistence.*

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

//    @OneToMany(mappedBy = "expense")
//    val expenses: List<ExpenseEntity>
) {

    fun toDomain() = PaymentMethod(
        id = id,
        method = method,
        financialAccount = financialAccount?.toDomain()
    )

    companion object {
        fun fromDomain(paymentMethod: PaymentMethod?) = PaymentMethodEntity(
            id = paymentMethod?.id,
            method = paymentMethod?.method,
            financialAccount = FinancialAccountEntity.fromDomain(paymentMethod?.financialAccount)
        )
    }
}