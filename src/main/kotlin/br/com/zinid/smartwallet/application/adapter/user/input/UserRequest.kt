package br.com.zinid.smartwallet.application.adapter.user.input

import br.com.zinid.smartwallet.domain.user.User
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UserRequest(
    @field:NotBlank
    @Size(min = 2, max = 255)
    val firstname: String,

    @field:NotBlank
    @Size(min = 2, max = 255)
    val lastname: String,

    @field:NotBlank
    @field:Email
    val email: String,

    @field:NotBlank
    @Size(min = 2, max = 20)
    val phone: String,
) {
    fun toDomain() = User(
        firstname = firstname,
        lastname = lastname,
        email = email,
        phone = phone
    )
}