package br.com.zinid.smartwallet.application.adapter.paymentmethod

import br.com.zinid.smartwallet.domain.paymentmethod.FindPaymentMethodInputPort
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/payment-methods")
class PaymentMethodController(
    private val findPaymentMethodUseCase: FindPaymentMethodInputPort
) {

    @GetMapping
    fun findByFinancialAccountId(@RequestParam financialAccountId: Long): ResponseEntity<Any?> =
        ResponseEntity.ok(
            findPaymentMethodUseCase.findByFinancialAccountId(financialAccountId)
                .map { PaymentMethodResponse.fromDomain(it) }
        )
}
