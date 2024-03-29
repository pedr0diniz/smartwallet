package br.com.zinid.smartwallet.application.adapter.user.output

import br.com.zinid.smartwallet.application.adapter.financialaccount.output.FinancialAccountEntity
import br.com.zinid.smartwallet.domain.user.User
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import org.hibernate.Hibernate
import org.springframework.dao.DataIntegrityViolationException

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
        id = id ?: throw DataIntegrityViolationException("Entity has no ID"),
        firstname = firstname ?: "",
        lastname = lastname ?: "",
        email = email ?: "",
        phone = phone ?: ""
    )
    companion object {
        fun fromDomain(user: User?) = UserEntity(
            id = if (user?.id == 0L) null else user?.id,
            firstname = user?.firstname,
            lastname = user?.lastname,
            email = user?.email,
            phone = user?.phone
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as UserEntity

        return (id != null) && (id == other.id)
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String =
        this::class.simpleName + "(id = $id , firstname = $firstname , lastname = $lastname , " +
            "email = $email , phone = $phone )"
}
