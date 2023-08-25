package br.com.zinid.smartwallet.application.config.beans

import br.com.zinid.smartwallet.application.adapter.user.output.CreateUserAdapter
import br.com.zinid.smartwallet.application.adapter.user.output.FindUserAdapter
import br.com.zinid.smartwallet.domain.user.input.CreateUserInputPort
import br.com.zinid.smartwallet.domain.user.input.CreateUserUseCase
import br.com.zinid.smartwallet.domain.user.input.FindUserInputPort
import br.com.zinid.smartwallet.domain.user.input.FindUserUseCase
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserConfig {

    @Bean
    fun createUserInputPort(createUserAdapter: CreateUserAdapter): CreateUserInputPort =
        CreateUserUseCase(createUserAdapter)

    @Bean
    fun findUserInputPort(findUserAdapter: FindUserAdapter): FindUserInputPort =
        FindUserUseCase(findUserAdapter)
}
