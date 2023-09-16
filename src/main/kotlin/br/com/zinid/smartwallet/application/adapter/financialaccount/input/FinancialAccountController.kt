package br.com.zinid.smartwallet.application.adapter.financialaccount.input

import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountResponse
import br.com.zinid.smartwallet.domain.financialaccount.input.CreateFinancialAccountInputPort
import br.com.zinid.smartwallet.domain.financialaccount.input.FindFinancialAccountInputPort
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
@RequestMapping("/financial-accounts")
class FinancialAccountController(
    private val createFinancialAccountUseCase: CreateFinancialAccountInputPort,
    private val findFinancialAccountUseCase: FindFinancialAccountInputPort
) {

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "201",
                content = [
                    Content(
                        schema = Schema(implementation = FinancialAccountResponse::class)
                    )
                ]
            )
        ]
    )
    @PostMapping
    fun create(@Valid @RequestBody financialAccountRequest: FinancialAccountRequest): ResponseEntity<Any?> {
        val possibleFinancialAccount = createFinancialAccountUseCase.execute(financialAccountRequest.toDomain())

        return ResponseEntity.status(HttpStatus.CREATED).body(
            FinancialAccountResponse.fromDomain(possibleFinancialAccount)
        )
    }

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        array = ArraySchema(schema = Schema(implementation = FinancialAccountResponse::class))
                    )
                ]
            )
        ]
    )
    @GetMapping
    fun findByUserId(@RequestParam userId: Long): ResponseEntity<Any?> =
        ResponseEntity.ok(
            findFinancialAccountUseCase.findByUserId(userId).map { FinancialAccountResponse.fromDomain(it) }
        )
}
