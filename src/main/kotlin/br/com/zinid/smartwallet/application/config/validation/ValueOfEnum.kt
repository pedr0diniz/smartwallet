package br.com.zinid.smartwallet.application.config.validation

import jakarta.validation.Constraint
import jakarta.validation.Payload
import kotlin.reflect.KClass


@MustBeDocumented
@Constraint(validatedBy = [ValueOfEnumValidator::class]) //
@Target(AnnotationTarget.FIELD) // aplicável em atributos
@Retention(AnnotationRetention.RUNTIME) // pode ser lida em runtime na aplicação

annotation class ValueOfEnum(
    val enumClass: KClass<out Enum<*>>,
    val message: String = "is not a valid option.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)