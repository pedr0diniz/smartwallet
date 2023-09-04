package br.com.zinid.smartwallet.application.adapter.expense.credit.input

import br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output.FindCreditCardInstallmentsAdapter
import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreditExpenseRepository
import br.com.zinid.smartwallet.application.adapter.expense.credit.output.CreditExpenseResponse
import br.com.zinid.smartwallet.domain.expense.credit.input.CreateCreditExpenseInputPort
import br.com.zinid.smartwallet.domain.expense.credit.input.FindCreditExpenseInputPort
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/expenses/credit")
class CreditExpenseController(
    private val createCreditExpenseUseCase: CreateCreditExpenseInputPort,
    private val findCreditExpenseUseCase: FindCreditExpenseInputPort,
    private val creditExpenseRepository: CreditExpenseRepository,
    private val findCreditCardInstallmentsAdapter: FindCreditCardInstallmentsAdapter
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

    @GetMapping("/{id}")
    fun findById(@PathVariable id: Long): ResponseEntity<Any?> {
        val ce = creditExpenseRepository.findById(id)
        val cci = findCreditCardInstallmentsAdapter.findByCreditExpenseId(id)
        if (ce.isPresent) return ResponseEntity.ok(CreditExpenseResponse.fromDomain(ce.get().toDomain().apply { this.creditCardInstallments = cci }))
        else return ResponseEntity.notFound().build()
    }
}
