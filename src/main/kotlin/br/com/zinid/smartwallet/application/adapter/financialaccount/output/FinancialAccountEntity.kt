package br.com.zinid.smartwallet.application.adapter.financialaccount.output

import br.com.zinid.smartwallet.application.adapter.paymentmethod.PaymentMethodEntity
import br.com.zinid.smartwallet.application.adapter.user.output.UserEntity
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.user.User
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.Hibernate
import org.springframework.dao.DataIntegrityViolationException
import java.math.BigDecimal

@Entity
@Table(name = "financial_account")
data class FinancialAccountEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val institution: String? = null,
    val balance: BigDecimal? = null,
    val overdraft: BigDecimal? = null,

    @OneToMany(mappedBy = "financialAccount")
    val paymentMethods: List<PaymentMethodEntity>? = listOf(),

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    val user: UserEntity? = null,
) {

    fun toDomain() = FinancialAccount(
        id = id ?: throw DataIntegrityViolationException("Entity has no ID"),
        institution = institution ?: "",
        balance = balance ?: BigDecimal.ZERO,
        overdraft = overdraft ?: BigDecimal.ZERO,
        user = user?.toDomain() ?: User.createBlank()
    )
    companion object {
        fun fromDomain(financialAccount: FinancialAccount?) = FinancialAccountEntity(
            id = if (financialAccount?.id == 0L) null else financialAccount?.id,
            institution = financialAccount?.institution,
            balance = financialAccount?.balance,
            overdraft = financialAccount?.overdraft,
            user = UserEntity.fromDomain(financialAccount?.user)
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as FinancialAccountEntity

        return (id != null) && (id == other.id)
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String =
        this::class.simpleName + "(id = $id , institution = $institution , balance = $balance , " +
            "overdraft = $overdraft , user = $user )"
}
