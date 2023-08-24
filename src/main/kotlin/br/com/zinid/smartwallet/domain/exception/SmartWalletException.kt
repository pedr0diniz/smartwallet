package br.com.zinid.smartwallet.domain.exception

abstract class SmartWalletException(
    override val message: String?
) : RuntimeException(message)

class InsufficientBalanceException(message: String?) : SmartWalletException(message)

class InsufficientCardLimitException(message: String?) : SmartWalletException(message)

class ExpiredCardException(message: String?) : SmartWalletException(message)

class InvalidDateRangeException(message: String?) : SmartWalletException(message)

class NoInstallmentsException(message: String?) : SmartWalletException(message)

class NoExplicitClassException(message: String?) : SmartWalletException(message)

class NotFoundException(clazz: DomainClasses, message: String?) : SmartWalletException(message) {
    companion object {
        fun buildFrom(clazz: DomainClasses, parameterName: String, parameterValue: Any) = NotFoundException(
            clazz = clazz,
            message = NOT_FOUND_MESSAGE.format(clazz, parameterName, parameterValue)
        )

        private const val NOT_FOUND_MESSAGE = "[%s] not found when searching by [%s = %s]"
    }
}

enum class DomainClasses {
    USER,
    FINANCIAL_ACCOUNT,
    FINANCIAL_ACCOUNTS,
    PAYMENT_METHOD,
    PAYMENT_METHODS,
    DEBIT_PAYMENT_METHOD,
    DEBIT_PAYMENT_METHODS,
    CREDIT_CARD,
    CREDIT_CARDS,
    EXPENSE,
    EXPENSES,
    DEBIT_EXPENSE,
    DEBIT_EXPENSES,
    CREDIT_EXPENSE,
    CREDIT_EXPENSES,
    CREDIT_CARD_INSTALLMENTS
}
