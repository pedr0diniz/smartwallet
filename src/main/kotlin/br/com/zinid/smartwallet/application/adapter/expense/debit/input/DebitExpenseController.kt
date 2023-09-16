package br.com.zinid.smartwallet.application.adapter.expense.debit.input

import br.com.zinid.smartwallet.application.adapter.expense.debit.output.DebitExpenseResponse
import br.com.zinid.smartwallet.domain.expense.debit.input.CreateDebitExpenseInputPort
import br.com.zinid.smartwallet.domain.expense.debit.input.FindDebitExpenseInputPort
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
@RequestMapping("/expenses/debit")
class DebitExpenseController(
    private val createDebitExpenseUseCase: CreateDebitExpenseInputPort,
    private val findDebitExpenseUseCase: FindDebitExpenseInputPort
) {

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                content = [
                    Content(
                        schema = Schema(implementation = DebitExpenseResponse::class)
                    )
                ]
            )
        ]
    )
    @PostMapping
    fun create(@Valid @RequestBody debitExpenseRequest: DebitExpenseRequest): ResponseEntity<Any?> {
        val possibleDebitExpense = createDebitExpenseUseCase.execute(debitExpenseRequest.toDomain())

        return ResponseEntity.status(HttpStatus.CREATED).body(
            DebitExpenseResponse.fromDomain(possibleDebitExpense)
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        array = ArraySchema(schema = Schema(implementation = DebitExpenseResponse::class))
                    )
                ]
            )
        ]
    )
    @GetMapping
    fun findByDebitPaymentMethodId(@RequestParam debitPaymentMethodId: Long): ResponseEntity<Any?> =
        ResponseEntity.ok(
            findDebitExpenseUseCase.findByDebitPaymentMethodId(debitPaymentMethodId)
                .map { DebitExpenseResponse.fromDomain(it) }
        )
}
