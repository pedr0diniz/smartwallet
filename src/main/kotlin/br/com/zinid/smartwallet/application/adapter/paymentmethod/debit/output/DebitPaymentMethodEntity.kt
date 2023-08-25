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
import org.hibernate.Hibernate
import org.springframework.dao.DataIntegrityViolationException

@Entity
@Table(name = "debit_payment_method")
data class DebitPaymentMethodEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val type: String? = null,

    @OneToOne
    @JoinColumn(name = "financial_account_id", referencedColumnName = "id", nullable = false)
    val financialAccount: FinancialAccountEntity? = null,
) {
    fun toDomain(debitExpenses: List<DebitExpense>? = emptyList()) = DebitPaymentMethod(
        id = id ?: throw DataIntegrityViolationException("Entity has no ID"),
        type = PaymentType.valueOf(type!!),
        financialAccount = financialAccount?.toDomain() ?: FinancialAccount.createBlank(),
        expenses = debitExpenses ?: emptyList()
    )

    companion object {
        fun fromDomain(debitPaymentMethod: DebitPaymentMethod?) = DebitPaymentMethodEntity(
            id = if (debitPaymentMethod?.id == 0L) null else debitPaymentMethod?.id,
            type = debitPaymentMethod?.type?.name,
            financialAccount = FinancialAccountEntity.fromDomain(debitPaymentMethod?.financialAccount)
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as DebitPaymentMethodEntity

        return (id != null) && (id == other.id)
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String =
        this::class.simpleName + "(id = $id , type = $type , financialAccount = $financialAccount )"
}
