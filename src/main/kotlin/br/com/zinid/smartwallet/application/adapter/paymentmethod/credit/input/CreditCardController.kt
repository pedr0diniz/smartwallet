package br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.input

import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.CreditCardResponse
import br.com.zinid.smartwallet.domain.paymentmethod.credit.input.CreateCreditCardInputPort
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/payment-methods")
class CreditCardController(
    private val createCreditCardUseCase: CreateCreditCardInputPort
) {

    @PostMapping("/credit")
    fun create(@Valid @RequestBody creditCardRequest: CreditCardRequest): ResponseEntity<Any?> {
        val possibleCreditCard = createCreditCardUseCase.execute(creditCardRequest.toDomain())
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

        return ResponseEntity.status(HttpStatus.CREATED).body(
            CreditCardResponse.fromDomain(possibleCreditCard)
        )
    }
}
