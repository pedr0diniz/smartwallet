package br.com.zinid.smartwallet.application.adapter.paymentmethod

import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountEntity
import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.CreditCardEntity
import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.DebitPaymentMethodEntity
import br.com.zinid.smartwallet.domain.expense.Expense
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
import br.com.zinid.smartwallet.domain.paymentmethod.PaymentMethod
import br.com.zinid.smartwallet.domain.paymentmethod.credit.CreditCard
import br.com.zinid.smartwallet.domain.paymentmethod.debit.DebitPaymentMethod
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.Hibernate
import java.lang.IllegalStateException

@Entity
@Table(name = "payment_method")
data class PaymentMethodEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "credit_card_id", referencedColumnName = "id", nullable = true)
    val creditCard: CreditCardEntity? = null,

    @OneToOne
    @JoinColumn(name = "debit_payment_method_id", referencedColumnName = "id", nullable = true)
    val debitPaymentMethod: DebitPaymentMethodEntity? = null,

    @ManyToOne
    @JoinColumn(name = "financial_account_id", referencedColumnName = "id", nullable = false)
    val financialAccount: FinancialAccountEntity? = null
) {
    fun toDomain(expenses: List<Expense>? = emptyList()): PaymentMethod {
        if (creditCard != null) {
            return creditCard.toDomain(expenses?.map { it as CreditExpense })
        } else if (debitPaymentMethod != null) {
            return debitPaymentMethod.toDomain(expenses?.map { it as DebitExpense })
        }

        throw IllegalStateException("Payment method must either be credit or debit")
    }

    companion object {
        fun fromCreditDomain(creditCard: CreditCard?) =
            CreditCardEntity.fromDomain(creditCard)

        fun fromDebitDomain(debitPaymentMethod: DebitPaymentMethod?) =
            DebitPaymentMethodEntity.fromDomain(debitPaymentMethod)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as PaymentMethodEntity

        return (id != null) && (id == other.id)
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String =
        this::class.simpleName + "(id = $id , creditCard = $creditCard , " +
            "debitPaymentMethod = $debitPaymentMethod , financialAccount = $financialAccount )"
}
