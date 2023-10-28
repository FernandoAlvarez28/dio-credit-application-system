package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.javafaker.Faker
import me.dio.credit.application.system.AbstractIntegrationTest
import me.dio.credit.application.system.dto.request.CustomerDto
import me.dio.credit.application.system.dto.request.CustomerUpdateDto
import me.dio.credit.application.system.repository.CustomerRepository
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal

class CustomerResourceTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val faker = Faker.instance()

    @Nested
    inner class SaveCustomer {

        @Test
        fun `Given valid Customer, then return status 201`() {
            // given
            val customerDto = mockCustomerDto(
                cpf = "618.064.590-60",
            )

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(customerDto)),
            )
                .andExpect(status().isCreated)
        }

        @Test
        fun `Given valid Customer, then return saved Customer`() {
            // given
            val customerDto = mockCustomerDto(
                cpf = "562.931.900-09",
            )

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(customerDto)),
            )
                .andExpect(jsonPath("$.id").isNotEmpty)
                .andExpect(jsonPath("$.firstName").value(customerDto.firstName))
                .andExpect(jsonPath("$.lastName").value(customerDto.lastName))
                .andExpect(jsonPath("$.cpf").value(customerDto.cpf))
                .andExpect(jsonPath("$.income").value(customerDto.income))
                .andExpect(jsonPath("$.email").value(customerDto.email))
                .andExpect(jsonPath("$.zipCode").value(customerDto.zipCode))
                .andExpect(jsonPath("$.street").value(customerDto.street))
        }

        @Test
        fun `Given valid Customer with existing CPF, then return status 409`() {
            // given
            val customerDto = mockCustomerDto(
                cpf = "977.416.090-84",
            )

            // when
            customerRepository.save(customerDto.toEntity())

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(customerDto)),
            )
                .andExpect(status().isConflict)
        }
    }

    @Nested
    inner class FindById {

        @Test
        fun `Given non existent Customer ID, then return status 400`() {
            // then
            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/customers/${faker.number().digits(8)}"),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun `Given existent Customer ID, then return saved Customer`() {
            // given
            val customerDto = mockCustomerDto(
                cpf = "268.152.570-53",
            )
            val savedCustomer = customerRepository.save(customerDto.toEntity())

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/customers/${savedCustomer.id}"),
            )
                .andExpect(jsonPath("$.id").value(savedCustomer.id))
                .andExpect(jsonPath("$.firstName").value(savedCustomer.firstName))
                .andExpect(jsonPath("$.lastName").value(savedCustomer.lastName))
                .andExpect(jsonPath("$.cpf").value(savedCustomer.cpf))
                .andExpect(jsonPath("$.income").value(savedCustomer.income))
                .andExpect(jsonPath("$.email").value(savedCustomer.email))
                .andExpect(jsonPath("$.zipCode").value(savedCustomer.address.zipCode))
                .andExpect(jsonPath("$.street").value(savedCustomer.address.street))
        }
    }

    @Nested
    inner class Delete {
        @Test
        fun `Given non existent Customer ID, then return status 400`() {
            // then
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/customers/${faker.number().digits(8)}"),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun `Given existent Customer ID, then return status 204`() {
            // given
            val customerDto = mockCustomerDto(
                cpf = "078.144.280-09",
            )
            val existentCustomer = customerRepository.save(customerDto.toEntity())

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.delete("/api/customers/${existentCustomer.id}"),
            )
                .andExpect(status().isNoContent)
                .andExpect(jsonPath("$").doesNotExist())
        }
    }

    @Nested
    inner class UpdateCustomer {
        @Test
        fun `Given valid Customer, but non existent Customer ID, then return status 400`() {
            // given
            val customerUpdateDto = mockCustomerUpdateDto()

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("customerId", faker.number().digits(8))
                    .content(objectMapper.writeValueAsString(customerUpdateDto)),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun `Given valid Customer, and existent Customer Id, then return status 200`() {
            // given
            val customerUpdateDto = mockCustomerUpdateDto()

            // when
            val customerDto = mockCustomerDto(
                cpf = "121.072.080-93",
            )
            val existentCustomer = customerRepository.save(customerDto.toEntity())

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("customerId", existentCustomer.id.toString())
                    .content(objectMapper.writeValueAsString(customerUpdateDto)),
            )
                .andExpect(status().isOk)
        }

        @Test
        fun `Given valid Customer, then return saved Customer`() {
            // given
            val customerUpdateDto = mockCustomerUpdateDto()

            // when
            val customerDto = mockCustomerDto(
                cpf = "799.770.910-05",
            )
            val existentCustomer = customerRepository.save(customerDto.toEntity())

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.patch("/api/customers")
                    .contentType(MediaType.APPLICATION_JSON)
                    .queryParam("customerId", existentCustomer.id.toString())
                    .content(objectMapper.writeValueAsString(customerUpdateDto)),
            )
                .andExpect(jsonPath("$.id").isNotEmpty)
                .andExpect(jsonPath("$.firstName").value(customerUpdateDto.firstName))
                .andExpect(jsonPath("$.lastName").value(customerUpdateDto.lastName))
                .andExpect(jsonPath("$.cpf").value(customerDto.cpf))
                .andExpect(jsonPath("$.income").value(customerUpdateDto.income))
                .andExpect(jsonPath("$.email").value(customerDto.email))
                .andExpect(jsonPath("$.zipCode").value(customerUpdateDto.zipCode))
                .andExpect(jsonPath("$.street").value(customerUpdateDto.street))
        }
    }

    private fun mockCustomerDto(
        cpf: String,
    ) = CustomerDto(
        firstName = "Fernando",
        lastName = "Alvarez",
        cpf = cpf,
        income = BigDecimal("8000"),
        email = faker.internet().emailAddress(),
        password = "youdontknow",
        zipCode = faker.address().zipCode(),
        street = faker.address().streetAddress(),
    )

    private fun mockCustomerUpdateDto() = CustomerUpdateDto(
        firstName = "Fê",
        lastName = "All",
        income = BigDecimal("9000"),
        zipCode = faker.address().zipCode(),
        street = faker.address().streetAddress(),
    )
}
