package br.com.zinid.smartwallet.application.adapter.user.output

import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository : JpaRepository<UserEntity, Long>
