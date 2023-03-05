package br.com.zinid.smartwallet.application.adapter.creditcard.output

import br.com.zinid.smartwallet.domain.creditcard.CreditCard
import org.springframework.data.jpa.repository.JpaRepository

interface CreditCardRepository : JpaRepository<CreditCardEntity, Long> {

    fun findByPaymentMethodId(paymentMethodId: Long): CreditCardEntity?
}