package br.com.zinid.smartwallet.fixtures

import br.com.zinid.smartwallet.domain.user.User

object UserFixtures {

    fun getUser() = User(
        id = 2L,
        firstname = "User",
        lastname = "from Test",
        email = "user@user.com",
        phone = "+551199999-6666"
    )
}
