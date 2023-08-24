package br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.input

import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.DebitPaymentMethodResponse
import br.com.zinid.smartwallet.domain.paymentmethod.debit.input.CreateDebitPaymentMethodInputPort
import br.com.zinid.smartwallet.domain.paymentmethod.debit.input.FindDebitPaymentMethodInputPort
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/payment-methods/debit")
class DebitPaymentMethodController(
    private val createDebitPaymentMethodUseCase: CreateDebitPaymentMethodInputPort,
    private val findDebitPaymentMethodUseCase: FindDebitPaymentMethodInputPort
) {

    @PostMapping
    fun create(@Valid @RequestBody debitPaymentMethodRequest: DebitPaymentMethodRequest): ResponseEntity<Any?> {
        debitPaymentMethodRequest.toDomainList()
            .map { createDebitPaymentMethodUseCase.execute(it) }
            .let { paymentMethodList ->
                return if (paymentMethodList.isEmpty())
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).build()
                else ResponseEntity.status(HttpStatus.CREATED).body(
                    paymentMethodList.map { DebitPaymentMethodResponse.fromDomain(it) }
                )
            }
    }

    @GetMapping
    fun findByFinancialAccountId(@RequestParam financialAccountId: Long): ResponseEntity<Any?> =
        ResponseEntity.ok(
            findDebitPaymentMethodUseCase.findByFinancialAccountId(financialAccountId)
                .map { DebitPaymentMethodResponse.fromDomain(it) }
        )
}
