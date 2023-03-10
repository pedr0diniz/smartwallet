package br.com.zinid.smartwallet.application.adapter.user.output

import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountEntity
import br.com.zinid.smartwallet.domain.financialaccount.FinancialAccount
import br.com.zinid.smartwallet.domain.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinTable
import jakarta.persistence.OneToMany
import jakarta.persistence.Table

@Entity
@Table(name = "\"user\"")
data class UserEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    val firstname: String? = null,
    val lastname: String? = null,

    @Column(unique = true)
    val email: String? = null,
    val phone: String? = null,

    @OneToMany(mappedBy = "user")
    val financialAccounts: List<FinancialAccountEntity>? = listOf()
) {

    fun toDomain() = User(
        id = id,
        firstname = firstname ?: "",
        lastname = lastname ?: "",
        email = email ?: "",
        phone = phone ?: ""
    )
    companion object {
        fun fromDomain(user: User?) = UserEntity(
            id = user?.id,
            firstname = user?.firstname,
            lastname = user?.lastname,
            email = user?.email,
            phone = user?.phone
        )
    }
}
