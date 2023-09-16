package br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.input

import br.com.zinid.smartwallet.application.adapter.paymentmethod.credit.output.CreditCardResponse
import br.com.zinid.smartwallet.domain.paymentmethod.credit.input.CreateCreditCardInputPort
import br.com.zinid.smartwallet.domain.paymentmethod.credit.input.FindCreditCardInputPort
import io.swagger.v3.oas.annotations.media.ArraySchema
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
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

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                content = [
                    Content(
                        schema = Schema(implementation = CreditCardResponse::class)
                    )
                ]
            )
        ]
    )
    @PostMapping
    fun create(@Valid @RequestBody creditCardRequest: CreditCardRequest): ResponseEntity<Any?> {
        val possibleCreditCard = createCreditCardUseCase.execute(creditCardRequest.toDomain())

        return ResponseEntity.status(HttpStatus.CREATED).body(
            CreditCardResponse.fromDomain(possibleCreditCard)
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        array = ArraySchema(schema = Schema(implementation = CreditCardResponse::class))
                    )
                ]
            )
        ]
    )
    @GetMapping
    fun findByFinancialAccountId(@RequestParam financialAccountId: Long): ResponseEntity<Any?> =
        ResponseEntity.ok(
            findCreditCardUseCase.findByFinancialAccountId(financialAccountId).map { CreditCardResponse.fromDomain(it) }
        )
}
