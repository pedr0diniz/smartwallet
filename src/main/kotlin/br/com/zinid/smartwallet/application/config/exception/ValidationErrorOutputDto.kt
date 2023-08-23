package br.com.zinid.smartwallet.application.config.exception

data class ValidationErrorOutputDto(
    val globalErrorMessages: List<String> = listOf(),
    val fieldErrors: List<FieldErrorOutputDto> = listOf()
)