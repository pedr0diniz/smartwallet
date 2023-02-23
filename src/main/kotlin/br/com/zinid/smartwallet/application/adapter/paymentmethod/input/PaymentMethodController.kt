package br.com.zinid.smartwallet.application.adapter.paymentmethod.input

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/payment-methods")
class PaymentMethodController {

    @PostMapping
    fun create(@Valid @RequestBody paymentMethodRequest: PaymentMethodRequest): ResponseEntity<Any?> {
        return ResponseEntity.ok().body(paymentMethodRequest
//            .toDomain()
        )
    }
}