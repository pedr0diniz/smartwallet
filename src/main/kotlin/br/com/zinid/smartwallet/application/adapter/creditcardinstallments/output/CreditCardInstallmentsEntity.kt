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
import java.math.BigDecimal

@Entity
@Table(name = "credit_card_installments")
class CreditCardInstallmentsEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val numberOfMonths: Int? = null,
    val totalValue: BigDecimal? = null,
    val firstInstallmentValue: BigDecimal? = null,
    val installmentValue: BigDecimal? = null,
    val invoiceClosingDayOfMonth: Int? = null,

    @OneToOne
    @JoinColumn(name = "credit_expense_id", referencedColumnName = "id", nullable = false)
    val expense: CreditExpenseEntity? = null
) {

    fun toDomain(): CreditCardInstallments {
        val creditCardInstallments = CreditCardInstallments(
            id = id,
            numberOfMonths = numberOfMonths ?: 0,
            totalValue = totalValue ?: BigDecimal.ZERO,
            firstInstallmentValue = firstInstallmentValue ?: BigDecimal.ZERO,
            installmentValue = installmentValue ?: BigDecimal.ZERO,
            invoiceClosingDayOfMonth = invoiceClosingDayOfMonth ?: 1,
            expense = expense?.toDomain() ?: CreditExpense.createBlank()
        )

        return creditCardInstallments.copy(installments = creditCardInstallments.buildInstallmentsList())
    }

    companion object {
        fun fromDomain(creditCardInstallments: CreditCardInstallments?) = CreditCardInstallmentsEntity(
            id = creditCardInstallments?.id,
            numberOfMonths = creditCardInstallments?.numberOfMonths,
            totalValue = creditCardInstallments?.totalValue,
            firstInstallmentValue = creditCardInstallments?.firstInstallmentValue,
            installmentValue = creditCardInstallments?.installmentValue,
            invoiceClosingDayOfMonth = creditCardInstallments?.invoiceClosingDayOfMonth,
            expense = CreditExpenseEntity.fromDomain(creditCardInstallments?.expense)
        )
    }
}
