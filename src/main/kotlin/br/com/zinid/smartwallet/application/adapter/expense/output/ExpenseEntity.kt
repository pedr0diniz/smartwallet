package br.com.zinid.smartwallet.application.adapter.expense.output

import br.com.zinid.smartwallet.application.adapter.paymentmethod.output.PaymentMethodEntity
import br.com.zinid.smartwallet.domain.expense.Expense
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table
import java.math.BigDecimal
import java.time.LocalDate

@Entity
@Table(name = "expense")
data class ExpenseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val content: String? = null,
    val date: LocalDate? = null,
    val price: BigDecimal? = null,
    val essential: Boolean? = false,
    val monthlySubscription: Boolean? = false,

    @ManyToOne
    @JoinColumn(name = "payment_method_id", referencedColumnName = "id", nullable = false)
    val paymentMethod: PaymentMethodEntity

//    @ManyToOne
//    @JoinColumn(name = "payment_method_id", referencedColumnName = "id", nullable = false)
//    val expenseFor: AcquaintanceEntity
) {
    fun toDomain() = Expense(
        id = id,
        content = content ?: "",
        date = date ?: LocalDate.now(),
        price = price ?: BigDecimal.ZERO,
        essential = essential,
        monthlySubscription = monthlySubscription,
        paymentMethod = paymentMethod.toDomain()
    )

    companion object {
        fun fromDomain(expense: Expense?) = ExpenseEntity(
            id = expense?.id,
            content = expense?.content,
            date = expense?.date,
            price = expense?.price,
            essential = expense?.essential,
            monthlySubscription = expense?.monthlySubscription,
            paymentMethod = PaymentMethodEntity.fromDomain(expense?.paymentMethod)
        )
    }
}
