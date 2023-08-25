package br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output

import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreditExpenseEntity
import br.com.zinid.smartwallet.domain.creditcardinstallment.CreditCardInstallments
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.Hibernate
import org.springframework.dao.DataIntegrityViolationException
import java.math.BigDecimal

@Entity
@Table(name = "credit_card_installments")
data class CreditCardInstallmentsEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val numberOfMonths: Int? = null,
    val totalValue: BigDecimal? = null,
    val firstInstallmentValue: BigDecimal? = null,
    val installmentValue: BigDecimal? = null,
    val invoiceDueDayOfMonth: Int? = null,

    @OneToOne
    @JoinColumn(name = "credit_expense_id", referencedColumnName = "id", nullable = false)
    val expense: CreditExpenseEntity? = null
) {

    fun toDomain(): CreditCardInstallments {
        val creditCardInstallments = CreditCardInstallments(
            id = id ?: throw DataIntegrityViolationException("Entity has no ID"),
            numberOfMonths = numberOfMonths ?: 0,
            totalValue = totalValue ?: BigDecimal.ZERO,
            firstInstallmentValue = firstInstallmentValue ?: BigDecimal.ZERO,
            installmentValue = installmentValue ?: BigDecimal.ZERO,
            invoiceDueDayOfMonth = invoiceDueDayOfMonth ?: 1,
            expense = expense?.toDomain() ?: CreditExpense.createBlank()
        )

        return creditCardInstallments.copy(installments = creditCardInstallments.buildInstallmentsList())
    }

    companion object {
        fun fromDomain(creditCardInstallments: CreditCardInstallments?) = CreditCardInstallmentsEntity(
            id = if (creditCardInstallments?.id == 0L) null else creditCardInstallments?.id,
            numberOfMonths = creditCardInstallments?.numberOfMonths,
            totalValue = creditCardInstallments?.totalValue,
            firstInstallmentValue = creditCardInstallments?.firstInstallmentValue,
            installmentValue = creditCardInstallments?.installmentValue,
            invoiceDueDayOfMonth = creditCardInstallments?.invoiceDueDayOfMonth,
            expense = CreditExpenseEntity.fromDomain(creditCardInstallments?.expense)
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as CreditCardInstallmentsEntity

        return (id != null) && (id == other.id)
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String =
        this::class.simpleName + "(id = $id , numberOfMonths = $numberOfMonths , totalValue = $totalValue , " +
            "firstInstallmentValue = $firstInstallmentValue , installmentValue = $installmentValue , " +
            "invoiceDueDayOfMonth = $invoiceDueDayOfMonth , expense = $expense )"
}
