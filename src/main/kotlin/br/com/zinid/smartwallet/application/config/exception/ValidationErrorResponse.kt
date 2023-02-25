package br.com.zinid.smartwallet.application.config.exception

data class ValidationErrorResponse(
    val globalErrorMessages: MutableList<String> = mutableListOf<String>(),
    val fieldErrors: MutableList<FieldErrorResponse> = mutableListOf<FieldErrorResponse>()
)