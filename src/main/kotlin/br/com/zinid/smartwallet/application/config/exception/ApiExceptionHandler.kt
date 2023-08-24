package br.com.zinid.smartwallet.application.config.exception

import br.com.zinid.smartwallet.domain.exception.ExpiredCardException
import br.com.zinid.smartwallet.domain.exception.InsufficientBalanceException
import br.com.zinid.smartwallet.domain.exception.InsufficientCardLimitException
import br.com.zinid.smartwallet.domain.exception.InvalidDateRangeException
import br.com.zinid.smartwallet.domain.exception.NoExplicitClassException
import br.com.zinid.smartwallet.domain.exception.NoInstallmentsException
import br.com.zinid.smartwallet.domain.exception.NotFoundException
import br.com.zinid.smartwallet.domain.exception.SmartWalletException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.dao.DataAccessException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@RestControllerAdvice
class ApiExceptionHandler : ResponseEntityExceptionHandler() {

    @Autowired
    private val messageSource: MessageSource? = null

    override fun handleMethodArgumentNotValid(
        ex: MethodArgumentNotValidException,
        headers: HttpHeaders,
        status: HttpStatusCode,
        request: WebRequest
    ): ResponseEntity<Any>? {

        val globalErrors = ex.bindingResult.globalErrors
        val fieldErrors = ex.bindingResult.fieldErrors

        val validationErrors = buildValidationErrors(globalErrors, fieldErrors)
        val errorResponse = ErrorResponse(
            status = status.value(),
            title = "One or more fields has invalid values. Fill the field(s) correctly and try again.",
            fields = validationErrors
        )
        return handleExceptionInternal(ex, errorResponse, headers, HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(SmartWalletException::class)
    fun handleSmartWalletException(ex: SmartWalletException, request: WebRequest): ResponseEntity<Any>? {
        val httpStatus = when (ex) {
            is InsufficientBalanceException -> HttpStatus.UNPROCESSABLE_ENTITY
            is InsufficientCardLimitException -> HttpStatus.UNPROCESSABLE_ENTITY
            is ExpiredCardException -> HttpStatus.UNPROCESSABLE_ENTITY
            is InvalidDateRangeException -> HttpStatus.BAD_REQUEST
            is NoInstallmentsException -> HttpStatus.BAD_REQUEST
            is NoExplicitClassException -> HttpStatus.INTERNAL_SERVER_ERROR
            is NotFoundException -> HttpStatus.NOT_FOUND
            else -> HttpStatus.INTERNAL_SERVER_ERROR
        }

        val errorResponse = ErrorResponse(
            status = httpStatus.value(),
            title = ex.message ?: ""
        )

        return handleExceptionInternal(ex, errorResponse, HttpHeaders(), httpStatus, request)
    }

    @ExceptionHandler(DataAccessException::class)
    fun handleDatabaseException(ex: DataAccessException, request: WebRequest): ResponseEntity<Any>? {
        val httpStatus = HttpStatus.INTERNAL_SERVER_ERROR

        val errorResponse = ErrorResponse(
            status = httpStatus.value(),
            title = ex.message ?: ex.cause.toString()
        )

        return handleExceptionInternal(ex, errorResponse, HttpHeaders(), httpStatus, request)
    }

    fun buildValidationErrors(
        globalErrors: List<ObjectError?>,
        fieldErrors: List<FieldError>
    ): ValidationErrorOutputDto = ValidationErrorOutputDto(
        globalErrorMessages = globalErrors.map { getErrorMessage(it) },
        fieldErrors = fieldErrors.map { FieldErrorOutputDto(it.field, getErrorMessage(it)) }
    )

    fun getErrorMessage(error: ObjectError?): String =
        messageSource!!.getMessage(error!!, LocaleContextHolder.getLocale())
}
