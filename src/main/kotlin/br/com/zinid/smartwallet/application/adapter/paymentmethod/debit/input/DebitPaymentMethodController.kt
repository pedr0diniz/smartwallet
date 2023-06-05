package br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.input

import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.DebitPaymentMethodResponse
import br.com.zinid.smartwallet.domain.paymentmethod.debit.input.CreateDebitPaymentMethodInputPort
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/payment-methods")
class DebitPaymentMethodController(
    private val createDebitPaymentMethodUseCase: CreateDebitPaymentMethodInputPort
) {

    @PostMapping("debit")
    fun create(@Valid @RequestBody debitPaymentMethodRequest: DebitPaymentMethodRequest): ResponseEntity<Any?> {
        debitPaymentMethodRequest.toDomainList()
            .mapNotNull { createDebitPaymentMethodUseCase.execute(it) }
            .let { paymentMethodList ->
                return if (paymentMethodList.isEmpty())
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

                else ResponseEntity.status(HttpStatus.CREATED).body(
                    paymentMethodList.map { DebitPaymentMethodResponse.fromDomain(it) }
                )
            }
    }
}
