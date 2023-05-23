package br.com.zinid.smartwallet.domain.user

import org.junit.jupiter.api.Test

internal class UserTest {

    @Test
    fun `should create a blank user`() {
        val user = User.createBlank()

        assert(user.id != null)
        assert(user.firstname == "")
        assert(user.lastname == "")
        assert(user.email == "")
        assert(user.phone == "")
        assert(user.financialAccounts.isNullOrEmpty())
        assert(user.acquaintances.isNullOrEmpty())
    }

    @Test
    fun `should create a blank user from id`() {
        val id = 1L
        val user = User.createBlankFromId(id)

        assert(user.id == id)
        assert(user.firstname == "")
        assert(user.lastname == "")
        assert(user.email == "")
        assert(user.phone == "")
        assert(user.financialAccounts.isNullOrEmpty())
        assert(user.acquaintances.isNullOrEmpty())
    }
}