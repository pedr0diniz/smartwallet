package br.com.zinid.smartwallet.application.config.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class CollectionOfEnumValuesValidator : ConstraintValidator<CollectionOfEnumValues, Collection<String>> {
    private var acceptedValues: List<String> = mutableListOf()
    override fun initialize(toValidate: CollectionOfEnumValues) {
        acceptedValues = toValidate.enumClass.java.enumConstants.map { it.name }
    }

    override fun isValid(value: Collection<String>, context: ConstraintValidatorContext): Boolean {
        if (value.isEmpty()) {
            return false
        }

        for (method in value) {
            if (!acceptedValues.contains(method.trim().uppercase())) {
                return false
            }
        }

        return true
    }
}