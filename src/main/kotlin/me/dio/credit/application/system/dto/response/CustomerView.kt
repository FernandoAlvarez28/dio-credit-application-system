package me.dio.credit.application.system.dto.response

import io.swagger.v3.oas.annotations.media.Schema
import me.dio.credit.application.system.entity.Customer
import java.math.BigDecimal

@Schema(description = "Complete representation of a Customer")
data class CustomerView(
    val id: Long,
    val firstName: String,
    val lastName: String,
    val cpf: String,
    val income: BigDecimal,
    val email: String,
    val zipCode: String,
    val street: String,
) {
    constructor(customer: Customer) : this (
        id = customer.id!!,
        firstName = customer.firstName,
        lastName = customer.lastName,
        cpf = customer.cpf,
        income = customer.income,
        email = customer.email,
        zipCode = customer.address.zipCode,
        street = customer.address.street,
    )
}
