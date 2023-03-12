package br.com.zinid.smartwallet.application.config.exception

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.MessageSource
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.HttpStatusCode
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.validation.ObjectError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import java.util.function.Consumer


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

        val errors: ValidationErrorOutputDto = buildValidationErrors(globalErrors, fieldErrors)
        val validationErr = ValidationErrorResponse(
            status = status.value(),
            title = "One or more fields has invalid values. Fill the field(s) correctly and try again.",
            fields = errors
        )
        return handleExceptionInternal(ex, validationErr, headers, HttpStatus.BAD_REQUEST, request)
    }

    fun buildValidationErrors(
        globalErrors: List<ObjectError?>, fieldErrors: List<FieldError>
    ): ValidationErrorOutputDto {

        val validationErrors = ValidationErrorOutputDto()

        globalErrors.forEach(Consumer { error: ObjectError? ->
            validationErrors.globalErrorMessages.add(getErrorMessage(error))
        })

        fieldErrors.forEach(Consumer { error: FieldError ->
            val errorMessage = getErrorMessage(error)
            validationErrors.fieldErrors.add(FieldErrorOutputDto(error.field, errorMessage))
        })
        return validationErrors
    }

    fun getErrorMessage(error: ObjectError?): String {
        return messageSource!!.getMessage(error!!, LocaleContextHolder.getLocale())
    }
}