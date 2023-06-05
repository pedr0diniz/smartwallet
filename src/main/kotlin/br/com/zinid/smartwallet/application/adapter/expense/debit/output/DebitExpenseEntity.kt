package br.com.zinid.smartwallet.application.adapter.expense.debit.output

import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.DebitPaymentMethodEntity
import br.com.zinid.smartwallet.domain.expense.debit.DebitExpense
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
@Table(name = "debit_expense")
data class DebitExpenseEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val content: String? = null,
    val date: LocalDate? = null,
    val price: BigDecimal? = null,
    val essential: Boolean? = false,
    val monthlySubscription: Boolean? = false,

    @ManyToOne
    @JoinColumn(name = "debit_payment_method_id", referencedColumnName = "id", nullable = false)
    val paymentMethod: DebitPaymentMethodEntity,

    //    @ManyToOne
//    @JoinColumn(name = "payment_method_id", referencedColumnName = "id", nullable = false)
//    val expenseFor: AcquaintanceEntity
) {

    fun toDomain() = DebitExpense(
        id = id,
        content = content ?: "",
        date = date ?: LocalDate.now(),
        price = price ?: BigDecimal.ZERO,
        essential = essential,
        monthlySubscription = monthlySubscription,
        paymentMethod = paymentMethod.toDomain()
    )

    companion object {
        fun fromDomain(debitExpense: DebitExpense?) = DebitExpenseEntity(
            id = debitExpense?.id,
            content = debitExpense?.content,
            date = debitExpense?.date,
            price = debitExpense?.price,
            essential = debitExpense?.essential,
            monthlySubscription = debitExpense?.monthlySubscription,
            paymentMethod = DebitPaymentMethodEntity.fromDomain(debitExpense?.paymentMethod)
        )
    }
}
