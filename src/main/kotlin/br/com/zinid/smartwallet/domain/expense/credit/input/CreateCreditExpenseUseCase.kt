package br.com.zinid.smartwallet.domain.expense.credit.input

import br.com.zinid.smartwallet.domain.creditcardinstallment.output.CreateCreditCardInstallmentsOutputPort
import br.com.zinid.smartwallet.domain.exception.DomainClasses.CREDIT_CARD
import br.com.zinid.smartwallet.domain.exception.InsufficientCardLimitException
import br.com.zinid.smartwallet.domain.exception.NotFoundException
import br.com.zinid.smartwallet.domain.expense.credit.CreditExpense
import br.com.zinid.smartwallet.domain.expense.credit.output.CreateCreditExpenseOutputPort
import br.com.zinid.smartwallet.domain.paymentmethod.credit.output.FindCreditCardOutputPort

class CreateCreditExpenseUseCase(
    private val findCreditCardAdapter: FindCreditCardOutputPort,
    private val createCreditCardInstallmentsAdapter: CreateCreditCardInstallmentsOutputPort,
    private val createCreditExpenseAdapter: CreateCreditExpenseOutputPort
) : CreateCreditExpenseInputPort {

    override fun execute(creditExpense: CreditExpense): CreditExpense {
        val currentExpense = attachPaymentMethodToExpense(creditExpense)

        return processCreditExpense(currentExpense)
    }

    private fun attachPaymentMethodToExpense(creditExpense: CreditExpense): CreditExpense {
        val creditCardId = creditExpense.paymentMethod.id
        val possiblePaymentMethod = findCreditCardAdapter.findById(creditCardId)
            ?: throw NotFoundException.buildFrom(CREDIT_CARD, "id", creditCardId)

        return creditExpense.copy(paymentMethod = possiblePaymentMethod)
    }

    private fun processCreditExpense(creditExpense: CreditExpense): CreditExpense {
        return if (creditExpense.canBeMade()) {
            creditExpense.process()

            // TODO - fix this
            // either create installments from installments domain
            // or update the savedExpense in another way
            val savedExpense = createCreditExpenseAdapter.create(creditExpense)
                .apply { this.creditCardInstallments = creditExpense.creditCardInstallments?.copy(expense = this) }

            createCreditCardInstallmentsAdapter.createFromExpense(savedExpense)

            savedExpense
        } else {
            throw InsufficientCardLimitException(
                INSUFFICIENT_CARD_LIMIT_MESSAGE.format(
                    creditExpense.price,
                    creditExpense.paymentMethod.getRemainingSpendableValue()
                )
            )
        }
    }

    companion object {
        private const val INSUFFICIENT_CARD_LIMIT_MESSAGE =
            "Cannot make [R$ %s] purchase from remaining card limit of [R$ %s]"
    }
}
