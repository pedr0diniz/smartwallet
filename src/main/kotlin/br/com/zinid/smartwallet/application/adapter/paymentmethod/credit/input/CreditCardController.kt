package br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.input

import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.CreditCardResponse
import br.com.zinid.smartwallet.domain.paymentmethod.credit.input.CreateCreditCardInputPort
import br.com.zinid.smartwallet.domain.paymentmethod.credit.input.FindCreditCardInputPort
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
@RequestMapping("/payment-methods/credit")
class CreditCardController(
    private val createCreditCardUseCase: CreateCreditCardInputPort,
    private val findCreditCardUseCase: FindCreditCardInputPort
) {

    @PostMapping("/credit")
    fun create(@Valid @RequestBody creditCardRequest: CreditCardRequest): ResponseEntity<Any?> {
        val possibleCreditCard = createCreditCardUseCase.execute(creditCardRequest.toDomain())

        return ResponseEntity.status(HttpStatus.CREATED).body(
            CreditCardResponse.fromDomain(possibleCreditCard)
        )
    }

    @GetMapping
    fun findByFinancialAccountId(@RequestParam financialAccountId: Long): ResponseEntity<Any?> =
        ResponseEntity.ok(
            findCreditCardUseCase.findByFinancialAccountId(financialAccountId).map { CreditCardResponse.fromDomain(it) }
        )
}
