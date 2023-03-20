package br.com.zinid.smartwallet.application.config.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Constraint(validatedBy = [CreditMethodMustHaveACardValidator::class])
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)

annotation class CreditMethodMustHaveACard(
    val message: String = "credit method must have an attached credit card",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)