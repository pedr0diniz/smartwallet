package br.com.zinid.smartwallet.application.adapter.creditcardinstallments.input

import br.com.zinid.smartwallet.application.adapter.creditcardinstallments.output.CreditCardInstallmentsResponse
import br.com.zinid.smartwallet.application.config.exception.ErrorResponse
import br.com.zinid.smartwallet.domain.creditcardinstallment.input.FindCreditCardInstallmentsInputPort
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("expenses/credit/installments")
class CreditCardInstallmentsController(
    private val findCreditCardInstallmentsUseCase: FindCreditCardInstallmentsInputPort
) {

    @ApiResponses(
        value = [
            ApiResponse(
                responseCode = "200",
                content = [
                    Content(
                        schema = Schema(implementation = CreditCardInstallmentsResponse::class)
                    )
                ]
            ),
            ApiResponse(
                responseCode = "404",
                content = [
                    Content(
                        schema = Schema(implementation = ErrorResponse::class)
                    )
                ]
            )
        ]
    )
    @GetMapping
    fun findByCreditExpenseId(@RequestParam creditExpenseId: Long): ResponseEntity<Any?> {
        val creditCardInstallments = findCreditCardInstallmentsUseCase.findByCreditExpenseId(creditExpenseId)
        if (creditCardInstallments != null) {
            return ResponseEntity.ok(CreditCardInstallmentsResponse.fromDomain(creditCardInstallments))
        }

        return ResponseEntity.status(NOT_FOUND_STATUS_VALUE).body(
            ErrorResponse(NOT_FOUND_STATUS_VALUE, title = NO_INSTALLMENTS_FOUND_MESSAGE.format(creditExpenseId))
        )
    }

    companion object {
        private const val NO_INSTALLMENTS_FOUND_MESSAGE = "No installments found for credit expense with id [%s]"
        private const val NOT_FOUND_STATUS_VALUE = 400
    }
}
