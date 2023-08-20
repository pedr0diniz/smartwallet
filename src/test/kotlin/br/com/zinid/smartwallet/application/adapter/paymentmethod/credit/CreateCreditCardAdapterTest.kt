package br.com.zinid.smartwallet.application.adapter.paymentmethod.credit

import br.com.zinid.smartwallet.application.adapter.paymentmethod.PaymentMethodEntity
import br.com.zinid.smartwallet.application.adapter.paymentmethod.PaymentMethodRepository
import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.CreateCreditCardAdapter
import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.CreditCardEntity
import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.CreditCardRepository
import br.com.zinid.smartwallet.fixtures.CreditCardFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CreateCreditCardAdapterTest {

    private val creditCardRepository = mockk<CreditCardRepository>()
    private val paymentMethodRepository = mockk<PaymentMethodRepository>()

    private val createCreditCardAdapter = CreateCreditCardAdapter(
        creditCardRepository,
        paymentMethodRepository
    )

    @Test
    fun `should create credit card`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditCardEntity = CreditCardEntity.fromDomain(creditCard)
        val paymentMethodEntity = PaymentMethodEntity.from(creditCardEntity)

        every { creditCardRepository.save(creditCardEntity) } returns creditCardEntity
        every { paymentMethodRepository.save(any()) } returns paymentMethodEntity

        val createdCreditCard = createCreditCardAdapter.create(creditCard)

        verify(exactly = 1) { creditCardRepository.save(creditCardEntity) }
        verify(exactly = 1) { paymentMethodRepository.save(any()) }

        assertEquals(creditCard, createdCreditCard)
    }
}
