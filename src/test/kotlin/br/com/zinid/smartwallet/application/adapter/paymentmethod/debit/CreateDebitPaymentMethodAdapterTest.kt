package br.com.zinid.smartwallet.application.adapter.paymentmethod.debit

import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.CreateDebitPaymentMethodAdapter
import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.DebitPaymentMethodEntity
import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.DebitPaymentMethodRepository
import br.com.zinid.smartwallet.fixtures.DebitPaymentMethodFixtures
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

internal class CreateDebitPaymentMethodAdapterTest {

    private val debitPaymentMethodRepository = mockk<DebitPaymentMethodRepository>()
    private val createDebitPaymentMethodAdapter = CreateDebitPaymentMethodAdapter(debitPaymentMethodRepository)

    @Test
    fun `should create debit payment method`() {
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()
        val debitPaymentMethodEntity = DebitPaymentMethodEntity.fromDomain(debitPaymentMethod)

        every { debitPaymentMethodRepository.save(debitPaymentMethodEntity) } returns debitPaymentMethodEntity

        val createdDebitPaymentMethod = createDebitPaymentMethodAdapter.create(debitPaymentMethod)

        verify(exactly = 1) { debitPaymentMethodRepository.save(debitPaymentMethodEntity) }

        assertEquals(debitPaymentMethod, createdDebitPaymentMethod)
    }
}
