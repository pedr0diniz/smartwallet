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
import org.hibernate.Hibernate
import org.springframework.dao.DataIntegrityViolationException
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
    val tag: String? = null,

    @ManyToOne
    @JoinColumn(name = "credit_card_id", referencedColumnName = "id", nullable = false)
    val paymentMethod: CreditCardEntity,

    val numberOfInstallments: Int? = null
) {

    fun toDomain() = CreditExpense(
        id = id ?: throw DataIntegrityViolationException("Entity has no ID"),
        content = content ?: "",
        date = date ?: LocalDate.now(),
        price = price ?: BigDecimal.ZERO,
        essential = essential,
        monthlySubscription = monthlySubscription,
        paymentMethod = paymentMethod.toDomain(),
        numberOfInstallments = numberOfInstallments,
        tag = tag
    )

    companion object {
        fun fromDomain(creditExpense: CreditExpense?) = CreditExpenseEntity(
            id = if (creditExpense?.id == 0L) null else creditExpense?.id,
            content = creditExpense?.content,
            date = creditExpense?.date,
            price = creditExpense?.price,
            essential = creditExpense?.essential,
            monthlySubscription = creditExpense?.monthlySubscription,
            paymentMethod = CreditCardEntity.fromDomain(creditExpense?.paymentMethod),
            numberOfInstallments = creditExpense?.numberOfInstallments,
            tag = creditExpense?.tag
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as CreditExpenseEntity

        return (id != null) && (id == other.id)
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String =
        this::class.simpleName + "(id = $id , content = $content , date = $date , price = $price , " +
            "essential = $essential , monthlySubscription = $monthlySubscription , paymentMethod = $paymentMethod )"
}
