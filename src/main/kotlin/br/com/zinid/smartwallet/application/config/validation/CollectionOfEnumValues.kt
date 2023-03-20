package br.com.zinid.smartwallet.application.config.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [CollectionOfEnumValuesValidator::class])
@Target(AnnotationTarget.FIELD)
@Retention(AnnotationRetention.RUNTIME)

annotation class CollectionOfEnumValues(
    val enumClass: KClass<out Enum<*>>,
    val message: String = "has an invalid payment method.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)