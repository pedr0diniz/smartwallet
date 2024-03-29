package br.com.zinid.smartwallet.application.config.exception

import java.time.OffsetDateTime

data class ErrorResponse(
    val status: Int,
    val timestamp: OffsetDateTime = OffsetDateTime.now(),
    val title: String,
    val fields: ValidationErrorOutputDto? = null
)
