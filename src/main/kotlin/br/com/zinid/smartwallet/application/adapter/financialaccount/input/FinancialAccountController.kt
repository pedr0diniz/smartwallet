package br.com.zinid.smartwallet.application.adapter.financialaccount.input

import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/financial-accounts")
class FinancialAccountController {

    @PostMapping
    fun create(@Valid @RequestBody financialAccountRequest: FinancialAccountRequest): ResponseEntity<Any?> {
        // achar o usuario
        // criar a conta de instituicao financeira
        // salvar a conta
        return ResponseEntity.ok().body(financialAccountRequest.toDomain())
    }
}