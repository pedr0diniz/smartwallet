package br.com.zinid.smartwallet.application.adapter.expense.input

import br.com.zinid.smartwallet.domain.expense.input.CreateExpenseInputPort
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/expenses")
class ExpenseController(
    private val createExpenseUseCase: CreateExpenseInputPort
) {

    @PostMapping
    fun create(@Valid @RequestBody expenseRequest: ExpenseRequest): ResponseEntity<Any?> {
        return ResponseEntity.status(HttpStatus.CREATED).body(
            createExpenseUseCase.execute(expenseRequest.toDomain())
        )
    }
}