package br.com.zinid.smartwallet.application.adapter.paymentmethod.debit

import br.com.zinid.smartwallet.application.adapter.paymentmethod.PaymentMethodEntity
import br.com.zinid.smartwallet.application.adapter.paymentmethod.PaymentMethodRepository
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
    private val paymentMethodRepository = mockk<PaymentMethodRepository>()
    private val createDebitPaymentMethodAdapter = CreateDebitPaymentMethodAdapter(
        debitPaymentMethodRepository,
        paymentMethodRepository
    )

    @Test
    fun `should create debit payment method`() {
        val debitPaymentMethod = DebitPaymentMethodFixtures.getDebitPaymentMethod()
        val debitPaymentMethodEntity = DebitPaymentMethodEntity.fromDomain(debitPaymentMethod)
        val paymentMethodEntity = PaymentMethodEntity.from(debitPaymentMethodEntity)

        every { debitPaymentMethodRepository.save(debitPaymentMethodEntity) } returns debitPaymentMethodEntity
        every { paymentMethodRepository.save(any()) } returns paymentMethodEntity

        val createdDebitPaymentMethod = createDebitPaymentMethodAdapter.create(debitPaymentMethod)

        verify(exactly = 1) { debitPaymentMethodRepository.save(debitPaymentMethodEntity) }
        verify(exactly = 1) { paymentMethodRepository.save(any()) }

        assertEquals(debitPaymentMethod, createdDebitPaymentMethod)
    }
}
