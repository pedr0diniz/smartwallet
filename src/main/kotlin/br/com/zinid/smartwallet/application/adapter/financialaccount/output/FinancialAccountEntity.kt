package br.com.zinid.smartwallet.application.adapter.financialaccount.output

import br.com.zinid.smartwallet.application.adapter.paymentmethod.output.PaymentMethodEntity
import br.com.zinid.smartwallet.application.adapter.user.output.UserEntity
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "financial_account")
data class FinancialAccountEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val institution: String? = null,
    val balance: BigDecimal? = null,

    @OneToMany(mappedBy = "financialAccount")
    val paymentMethods: List<PaymentMethodEntity>? = listOf(),

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    val user: UserEntity? = null,
) {

    fun toDomain() = FinancialAccount(
        id = id,
        institution = institution,
        balance = balance,
        user = user?.toDomain()
    )
    companion object {
        fun fromDomain(financialAccount: FinancialAccount?) = FinancialAccountEntity(
            id = financialAccount?.id,
            institution = financialAccount?.institution,
            balance = financialAccount?.balance,
            user = UserEntity.fromDomain(financialAccount?.user)
        )
    }
}