package br.com.zinid.smartwallet.application.adapter.paymentmethod.credit

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
    private val createCreditCardAdapter = CreateCreditCardAdapter(creditCardRepository)

    @Test
    fun `should create credit card`() {
        val creditCard = CreditCardFixtures.getCreditCard()
        val creditCardEntity = CreditCardEntity.fromDomain(creditCard)

        every { creditCardRepository.save(creditCardEntity) } returns creditCardEntity

        val createdCreditCard = createCreditCardAdapter.create(creditCard)

        verify(exactly = 1) { creditCardRepository.save(creditCardEntity) }

        assertEquals(creditCard, createdCreditCard)
    }
}
