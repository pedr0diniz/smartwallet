package br.com.zinid.smartwallet.application.config.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.*


class ValueOfEnumValidator : ConstraintValidator<ValueOfEnum, Any?> {
    private var acceptedValues: List<String>? = null
    override fun initialize(toValidate: ValueOfEnum) {
        acceptedValues = toValidate.enumClass.java.enumConstants.map { it.name }
    }

    override fun isValid(value: Any?, context: ConstraintValidatorContext): Boolean {
        if (value != null) {
            return acceptedValues!!
                .contains(
                    value.toString().trim() { it <= ' ' }
                    .uppercase(Locale.getDefault())
                )
        }
        return false
    }
}