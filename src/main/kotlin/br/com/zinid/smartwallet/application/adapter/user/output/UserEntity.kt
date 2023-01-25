package br.com.zinid.smartwallet.application.adapter.user.output

import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "\"user\"")
data class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val firstname: String,
    val lastname: String,

    @Column(unique = true)
    val email: String,
    val phone: String,
//    val financialAccounts: List<FinancialAccount>?
) {
    companion object {
        fun fromDomain(user: User) = UserEntity(
            firstname = user.firstname,
            lastname = user.lastname,
            email = user.email,
            phone = user.phone
        )
    }
}
