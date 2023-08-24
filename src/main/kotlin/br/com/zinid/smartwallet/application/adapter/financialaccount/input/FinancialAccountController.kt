package br.com.zinid.smartwallet.application.adapter.financialaccount.input

import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountResponse
import br.com.zinid.smartwallet.domain.financialaccount.input.CreateFinancialAccountInputPort
import br.com.zinid.smartwallet.domain.financialaccount.input.FindFinancialAccountInputPort
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

    @PostMapping
    fun create(@Valid @RequestBody financialAccountRequest: FinancialAccountRequest): ResponseEntity<Any?> {
        val possibleFinancialAccount = createFinancialAccountUseCase.execute(financialAccountRequest.toDomain())

        return ResponseEntity.status(HttpStatus.CREATED).body(
            FinancialAccountResponse.fromDomain(possibleFinancialAccount)
        )
    }

    @GetMapping
    fun findByUserId(@RequestParam userId: Long): ResponseEntity<Any?> =
        ResponseEntity.ok(
            findFinancialAccountUseCase.findByUserId(userId).map { FinancialAccountResponse.fromDomain(it) }
        )
}
