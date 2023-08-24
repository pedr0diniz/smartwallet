package br.com.zinid.smartwallet.application.adapter.expense.credit.input

import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreditExpenseResponse
import br.com.zinid.smartwallet.domain.expense.credit.input.CreateCreditExpenseInputPort
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/expenses")
class CreditExpenseController(
    private val createCreditExpenseUseCase: CreateCreditExpenseInputPort
) {

    @PostMapping("/credit")
    fun create(@Valid @RequestBody creditExpenseRequest: CreditExpenseRequest): ResponseEntity<Any?> {
        val possibleCreditExpense = createCreditExpenseUseCase.execute(creditExpenseRequest.toDomain())
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

        return ResponseEntity.status(HttpStatus.CREATED).body(
            CreditExpenseResponse.fromDomain(possibleCreditExpense)
        )
    }
}
