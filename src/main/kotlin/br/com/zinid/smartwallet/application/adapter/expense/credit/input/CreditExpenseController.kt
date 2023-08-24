package br.com.zinid.smartwallet.application.adapter.expense.credit.input

import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreditExpenseResponse
import br.com.zinid.smartwallet.domain.expense.credit.input.CreateCreditExpenseInputPort
import br.com.zinid.smartwallet.domain.expense.credit.input.FindCreditExpenseInputPort
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
@RequestMapping("/expenses/credit")
class CreditExpenseController(
    private val createCreditExpenseUseCase: CreateCreditExpenseInputPort,
    private val findCreditExpenseUseCase: FindCreditExpenseInputPort
) {

    @PostMapping
    fun create(@Valid @RequestBody creditExpenseRequest: CreditExpenseRequest): ResponseEntity<Any?> {
        val possibleCreditExpense = createCreditExpenseUseCase.execute(creditExpenseRequest.toDomain())

        return ResponseEntity.status(HttpStatus.CREATED).body(
            CreditExpenseResponse.fromDomain(possibleCreditExpense)
        )
    }

    @GetMapping
    fun findByCreditCardId(@RequestParam creditCardId: Long): ResponseEntity<Any?> =
        ResponseEntity.ok(
            findCreditExpenseUseCase.findByCreditCardId(creditCardId).map { CreditExpenseResponse.fromDomain(it) }
        )
}
