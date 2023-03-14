package br.com.zinid.smartwallet.application.adapter.paymentmethod.input

import br.com.zinid.smartwallet.application.adapter.paymentmethod.output.PaymentMethodResponse
import br.com.zinid.smartwallet.domain.paymentmethod.input.CreatePaymentMethodInputPort
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/payment-methods")
class PaymentMethodController(
    private val createPaymentMethodUseCase: CreatePaymentMethodInputPort
) {

    @PostMapping
    fun create(@Valid @RequestBody paymentMethodRequest: PaymentMethodRequest): ResponseEntity<Any?> {
        val possiblePaymentMethod = createPaymentMethodUseCase.execute(paymentMethodRequest.toDomain())
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

        return ResponseEntity.status(HttpStatus.CREATED).body(
            PaymentMethodResponse.fromDomain(possiblePaymentMethod)
        )
    }
}