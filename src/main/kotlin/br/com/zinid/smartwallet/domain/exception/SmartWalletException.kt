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
