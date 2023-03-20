package br.com.zinid.smartwallet.application.adapter.user.input

import br.com.zinid.smartwallet.application.adapter.user.output.FindUserAdapter
import br.com.zinid.smartwallet.application.adapter.user.output.UserResponse
import br.com.zinid.smartwallet.domain.user.input.CreateUserInputPort
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/users")
class UserController(
    private val createUserUseCase: CreateUserInputPort,
    private val findUserAdapter: FindUserAdapter
) {

    @PostMapping
    fun create(@Valid @RequestBody userRequest: UserRequest): ResponseEntity<Any?> {
        val possibleUser = createUserUseCase.execute(userRequest.toDomain())
            ?: return ResponseEntity.status(HttpStatus.BAD_REQUEST).build()

        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponse.fromDomain(possibleUser))
    }
}