package br.com.zinid.smartwallet.application.config.validation

import br.com.zinid.smartwallet.application.adapter.paymentmethod.input.PaymentMethodRequest
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class CreditMethodMustHaveACardValidator : ConstraintValidator<CreditMethodMustHaveACard, PaymentMethodRequest> {

    override fun isValid(request: PaymentMethodRequest, context: ConstraintValidatorContext): Boolean {
        if (request.methods.contains("CREDIT") && request.creditCard == null) {
            return false
        }

        return true
    }
}