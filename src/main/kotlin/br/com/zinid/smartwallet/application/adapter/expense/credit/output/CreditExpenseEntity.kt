package br.com.zinid.smartwallet.application.adapter.expense.credit.output

import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.CreditCardEntity
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
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
@Table(name = "credit_expense")
data class CreditExpenseEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val content: String? = null,
    val date: LocalDate? = null,
    val price: BigDecimal? = null,
    val essential: Boolean? = false,
    val monthlySubscription: Boolean? = false,

    @ManyToOne
    @JoinColumn(name = "credit_card_id", referencedColumnName = "id", nullable = false)
    val paymentMethod: CreditCardEntity

    //    @ManyToOne
//    @JoinColumn(name = "payment_method_id", referencedColumnName = "id", nullable = false)
//    val expenseFor: AcquaintanceEntity
) {

    fun toDomain() = CreditExpense(
        id = id,
        content = content ?: "",
        date = date ?: LocalDate.now(),
        price = price ?: BigDecimal.ZERO,
        essential = essential,
        monthlySubscription = monthlySubscription,
        paymentMethod = paymentMethod.toDomain()
    )

    companion object {
        fun fromDomain(creditExpense: CreditExpense?) = CreditExpenseEntity(
            id = creditExpense?.id,
            content = creditExpense?.content,
            date = creditExpense?.date,
            price = creditExpense?.price,
            essential = creditExpense?.essential,
            monthlySubscription = creditExpense?.monthlySubscription,
            paymentMethod = CreditCardEntity.fromDomain(creditExpense?.paymentMethod)
        )
    }
}
