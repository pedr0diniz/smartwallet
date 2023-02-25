package br.com.zinid.smartwallet.application.adapter.creditcard.output

import br.com.zinid.smartwallet.application.adapter.paymentmethod.output.PaymentMethodEntity
import br.com.zinid.smartwallet.domain.creditcard.CreditCard
import jakarta.persistence.*
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
//    val installments: List<CreditCardInstallmentEntity>? = listOf()
    val invoiceClosingDate: LocalDate? = null,
    val invoiceDueDate: LocalDate? = null,

    @OneToOne
    @JoinColumn(name = "payment_method_id", referencedColumnName = "id", nullable = false)
    val paymentMethod: PaymentMethodEntity
) {

    companion object {
        fun fromDomain(creditCard: CreditCard?) = CreditCardEntity(
            id = creditCard?.id,
            last4Digits = creditCard?.last4Digits,
            expirationDate = creditCard?.expirationDate,
            cardLimit = creditCard?.cardLimit,
//            installments,
            invoiceClosingDate = creditCard?.invoiceClosingDate,
            invoiceDueDate = creditCard?.invoiceDueDate,
            paymentMethod = PaymentMethodEntity.fromDomain(creditCard?.paymentMethod)
        )
    }
}