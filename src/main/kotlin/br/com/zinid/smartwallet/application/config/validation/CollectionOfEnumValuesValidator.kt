package br.com.zinid.smartwallet.application.config.validation

import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext

class CollectionOfEnumValuesValidator : ConstraintValidator<CollectionOfEnumValues, Collection<String>> {
    private var acceptedValues: List<String> = mutableListOf()
    override fun initialize(toValidate: CollectionOfEnumValues) {
        acceptedValues = toValidate.enumClass.java.enumConstants.map { it.name }
    }

    override fun isValid(collection: Collection<String>, context: ConstraintValidatorContext): Boolean =
        !(collection.isEmpty() || hasAnInvalidValueIn(collection))

    private fun hasAnInvalidValueIn(collection: Collection<String>): Boolean {
        for (method in collection) {
            if (!acceptedValues.contains(method.trim().uppercase())) {
                return true
            }
        }

        return false
    }
}
