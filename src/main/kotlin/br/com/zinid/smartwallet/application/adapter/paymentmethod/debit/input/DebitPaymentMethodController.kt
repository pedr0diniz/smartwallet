package br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.input

import br.com.zinid.smartwallet.application.adapter.paymentmethod.debit.output.DebitPaymentMethodResponse
import br.com.zinid.smartwallet.application.config.exception.ErrorResponse
import br.com.zinid.smartwallet.domain.paymentmethod.debit.input.CreateDebitPaymentMethodInputPort
import br.com.zinid.smartwallet.domain.paymentmethod.debit.input.FindDebitPaymentMethodInputPort
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
@RequestMapping("/payment-methods/debit")
class DebitPaymentMethodController(
    private val createDebitPaymentMethodUseCase: CreateDebitPaymentMethodInputPort,
    private val findDebitPaymentMethodUseCase: FindDebitPaymentMethodInputPort
) {

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                content = [
                    Content(
                        array = ArraySchema(schema = Schema(implementation = DebitPaymentMethodResponse::class))
                    )
                ]
            ),
            ApiResponse(
                responseCode = "400",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorResponse::class)
                    )
                ]
            )
        ]
    )
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

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        array = ArraySchema(schema = Schema(implementation = DebitPaymentMethodResponse::class))
                    )
                ]
            )
        ]
    )
    @GetMapping
    fun findByFinancialAccountId(@RequestParam financialAccountId: Long): ResponseEntity<Any?> =
        ResponseEntity.ok(
            findDebitPaymentMethodUseCase.findByFinancialAccountId(financialAccountId)
                .map { DebitPaymentMethodResponse.fromDomain(it) }
        )
}
