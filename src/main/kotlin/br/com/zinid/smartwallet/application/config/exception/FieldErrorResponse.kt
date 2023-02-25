package br.com.zinid.smartwallet.application.config.exception

data class FieldErrorResponse(
    val field: String,
    val message: String
)