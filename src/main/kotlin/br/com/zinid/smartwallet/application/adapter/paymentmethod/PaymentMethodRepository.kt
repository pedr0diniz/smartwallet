package br.com.zinid.smartwallet.application.adapter.paymentmethod

import org.springframework.data.jpa.repository.JpaRepository

interface PaymentMethodRepository : JpaRepository<PaymentMethodEntity, Long>
