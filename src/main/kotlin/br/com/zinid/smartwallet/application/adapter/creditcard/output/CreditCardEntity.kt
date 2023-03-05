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
    val invoiceClosingDate: LocalDate? = null,
    val invoiceDueDate: LocalDate? = null,

    @OneToOne
    val paymentMethod: PaymentMethodEntity? = null,
) {
    fun toDomain() = CreditCard(
        id = id,
        last4Digits = last4Digits,
        expirationDate = expirationDate,
        cardLimit = cardLimit,
        invoiceClosingDate = invoiceClosingDate,
        invoiceDueDate = invoiceDueDate,
        paymentMethod = paymentMethod?.toDomain()
    )

    companion object {
        fun fromDomain(creditCard: CreditCard?) = CreditCardEntity(
            id = creditCard?.id,
            last4Digits = creditCard?.last4Digits,
            expirationDate = creditCard?.expirationDate,
            cardLimit = creditCard?.cardLimit,
            invoiceClosingDate = creditCard?.invoiceClosingDate,
            invoiceDueDate = creditCard?.invoiceDueDate,
            paymentMethod = PaymentMethodEntity.fromDomain(creditCard?.paymentMethod)
        )
    }
}