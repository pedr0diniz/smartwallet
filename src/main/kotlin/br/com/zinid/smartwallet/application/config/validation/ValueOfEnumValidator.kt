package br.com.zinid.smartwallet.application.config.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.Locale

class ValueOfEnumValidator : ConstraintValidator<ValueOfEnum, String?> {
    private var acceptedValues: List<String>? = null
    override fun initialize(toValidate: ValueOfEnum) {
        acceptedValues = toValidate.enumClass.java.enumConstants.map { it.name }
    }

    override fun isValid(value: String?, context: ConstraintValidatorContext): Boolean {
        if (value != null) {
            return acceptedValues!!
                .contains(
                    value.toString().trim { it <= ' ' }
                        .uppercase(Locale.getDefault())
                )
        }
        return false
    }
}
