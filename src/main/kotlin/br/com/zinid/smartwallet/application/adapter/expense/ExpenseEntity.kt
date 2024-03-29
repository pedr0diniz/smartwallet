package br.com.zinid.smartwallet.application.adapter.expense

import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreditExpenseEntity
import br.com.zinid.smartwallet.application.adapter.expense.debit.output.DebitExpenseEntity
import br.com.zinid.smartwallet.domain.exception.NoExplicitClassException
import br.com.zinid.smartwallet.domain.expense.Expense
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToOne
import jakarta.persistence.Table
import org.hibernate.Hibernate

@Entity
@Table(name = "expense")
data class ExpenseEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @OneToOne
    @JoinColumn(name = "credit_expense_id", referencedColumnName = "id", nullable = true)
    val creditExpense: CreditExpenseEntity? = null,

    @OneToOne
    @JoinColumn(name = "debit_expense_id", referencedColumnName = "id", nullable = true)
    val debitExpense: DebitExpenseEntity? = null,

) {
    fun toDomain(): Expense {
        if (creditExpense != null) {
            return creditExpense.toDomain()
        } else if (debitExpense != null) {
            return debitExpense.toDomain()
        }

        throw NoExplicitClassException("Expense must either be credit or debit")
    }

    companion object {
        fun from(creditExpenseEntity: CreditExpenseEntity) = ExpenseEntity(
            creditExpense = creditExpenseEntity
        )

        fun from(debitExpenseEntity: DebitExpenseEntity) = ExpenseEntity(
            debitExpense = debitExpenseEntity
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ExpenseEntity

        return (id != null) && (id == other.id)
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String =
        this::class.simpleName + "(id = $id , creditExpense = $creditExpense , debitExpense = $debitExpense )"
}
