package br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output

import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountEntity
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "credit_card")
data class CreditCardEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val last4Digits: String? = null,
    val expirationDate: LocalDate? = null,
    val cardLimit: BigDecimal? = null,
    val invoiceClosingDayOfMonth: Int? = null,

    @OneToOne
    @JoinColumn(name = "financial_account_id", referencedColumnName = "id", nullable = false)
    val financialAccount: FinancialAccountEntity? = null,
) {
    fun toDomain(creditExpenses: List<CreditExpense>? = emptyList()) = CreditCard(
        id = id,
        last4Digits = last4Digits ?: "",
        expirationDate = expirationDate ?: LocalDate.now(),
        cardLimit = cardLimit ?: BigDecimal.ZERO,
        invoiceClosingDayOfMonth = invoiceClosingDayOfMonth ?: 1,
        financialAccount = financialAccount?.toDomain() ?: FinancialAccount.createBlank(),
        expenses = creditExpenses
    )

    companion object {
        fun fromDomain(creditCard: CreditCard?) = CreditCardEntity(
            id = creditCard?.id,
            last4Digits = creditCard?.last4Digits,
            expirationDate = creditCard?.expirationDate,
            cardLimit = creditCard?.cardLimit,
            invoiceClosingDayOfMonth = creditCard?.invoiceClosingDayOfMonth,
            financialAccount = FinancialAccountEntity.fromDomain(creditCard?.financialAccount)
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as CreditCardEntity

        return (id != null) && (id == other.id)
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String =
        this::class.simpleName + "(" +
            "id = $id , last4Digits = $last4Digits , expirationDate = $expirationDate , cardLimit = $cardLimit , " +
            "invoiceClosingDayOfMonth = $invoiceClosingDayOfMonth , financialAccount = $financialAccount )"
}
