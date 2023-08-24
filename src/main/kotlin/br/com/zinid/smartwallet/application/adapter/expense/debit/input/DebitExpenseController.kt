package br.com.zinid.smartwallet.application.adapter.expense.debit.input

import br.com.zinid.smartwallet.application.adapter.expense.debit.output.DebitExpenseResponse
import br.com.zinid.smartwallet.domain.expense.debit.input.CreateDebitExpenseInputPort
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/expenses")
class DebitExpenseController(
    private val createDebitExpenseUseCase: CreateDebitExpenseInputPort
) {

    @PostMapping("/debit")
    fun create(@Valid @RequestBody debitExpenseRequest: DebitExpenseRequest): ResponseEntity<Any?> {
        val possibleDebitExpense = createDebitExpenseUseCase.execute(debitExpenseRequest.toDomain())
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

        return ResponseEntity.status(HttpStatus.CREATED).body(
            DebitExpenseResponse.fromDomain(possibleDebitExpense)
        )
    }
}
