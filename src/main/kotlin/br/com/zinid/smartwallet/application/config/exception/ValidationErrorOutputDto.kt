package br.com.zinid.smartwallet.application.config.exception

data class ValidationErrorOutputDto(
    val globalErrorMessages: MutableList<String> = mutableListOf<String>(),
    val fieldErrors: MutableList<FieldErrorOutputDto> = mutableListOf<FieldErrorOutputDto>()
)