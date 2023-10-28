package me.dio.credit.application.system.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.javafaker.Faker
import me.dio.credit.application.system.AbstractIntegrationTest
import me.dio.credit.application.system.dto.request.CreditDto
import me.dio.credit.application.system.dto.request.CustomerDto
import me.dio.credit.application.system.enummeration.Status
import me.dio.credit.application.system.repository.CreditRepository
import me.dio.credit.application.system.repository.CustomerRepository
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

class CreditResourceTest : AbstractIntegrationTest() {

    @Autowired
    private lateinit var customerRepository: CustomerRepository

    @Autowired
    private lateinit var creditRepository: CreditRepository

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var objectMapper: ObjectMapper

    private val faker = Faker.instance()

    @Nested
    inner class SaveCredit {
        @Test
        fun `Given valid Credit, but non existent Customer ID, then return status 400`() {
            // given
            val creditDto = mockCreditDto(
                customerId = faker.number().digits(8).toLong(),
            )

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/credits")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(creditDto)),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun `Given valid Credit, with existent Customer ID, then return status 201`() {
            // given
            val savedCustomer = mockSavedCustomer("187.568.510-30")
            val creditDto = mockCreditDto(
                customerId = savedCustomer.id!!,
            )

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/credits")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(creditDto)),
            )
                .andExpect(status().isCreated)
        }

        @Test
        fun `Given valid Credit, with existent Customer ID, then return saved Credit`() {
            // given
            val savedCustomer = mockSavedCustomer("723.048.090-65")
            val creditDto = mockCreditDto(
                customerId = savedCustomer.id!!,
            )

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/credits")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(creditDto)),
            )
                .andExpect(jsonPath("$.creditCode").isNotEmpty)
                .andExpect(jsonPath("$.creditValue").value(creditDto.creditValue))
                .andExpect(jsonPath("$.numberOfInstallment").value(creditDto.numberOfInstallments))
                .andExpect(jsonPath("$.status").value(Status.IN_PROGRESS.name))
                .andExpect(jsonPath("$.emailCustomer").value(savedCustomer.email))
                .andExpect(jsonPath("$.incomeCustomer").value(savedCustomer.income))
        }

        @Test
        fun `Given Credit with invalid number of installments, and existent Customer ID, then return status 400`() {
            // given
            val savedCustomer = mockSavedCustomer("956.877.810-13")
            val creditDto = mockCreditDto(
                numberOfInstallments = 300,
                customerId = savedCustomer.id!!,
            )

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/credits")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(creditDto)),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun `Given Credit with day first of installment as today, and existent Customer ID, then return status 400`() {
            // given
            val savedCustomer = mockSavedCustomer("936.955.830-61")
            val creditDto = mockCreditDto(
                dayFirstOfInstallment = LocalDate.now(),
                customerId = savedCustomer.id!!,
            )

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/credits")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(creditDto)),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun `Given Credit with past day first of installment, and existent Customer ID, then return status 400`() {
            // given
            val savedCustomer = mockSavedCustomer("954.039.570-49")
            val creditDto = mockCreditDto(
                dayFirstOfInstallment = LocalDate.now().minusDays(1),
                customerId = savedCustomer.id!!,
            )

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/credits")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(creditDto)),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun `Given Credit with day first of installment further than 3 months, and existent Customer ID, then return status 400`() {
            // given
            val savedCustomer = mockSavedCustomer("086.040.580-01")
            val creditDto = mockCreditDto(
                dayFirstOfInstallment = LocalDate.now().plusMonths(3),
                customerId = savedCustomer.id!!,
            )

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.post("/api/credits")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(creditDto)),
            )
                .andExpect(status().isBadRequest)
        }
    }

    @Nested
    inner class FindAllByCustomerId {
        @Test
        fun `Given non existent Customer ID, then return status 200`() {
            // then
            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/credits")
                    .queryParam("customerId", faker.number().digits(8)),
            )
                .andExpect(status().isOk)
        }

        @Test
        fun `Given existent Customer ID, but without Credits, then return status 200`() {
            // given
            val savedCustomer = mockSavedCustomer(cpf = "653.833.330-37")

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/credits")
                    .queryParam("customerId", savedCustomer.id.toString()),
            )
                .andExpect(status().isOk)
        }

        @Test
        fun `Given existent Customer ID, but without Credits, then return empty body`() {
            // given
            val savedCustomer = mockSavedCustomer(cpf = "238.048.560-71")

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/credits")
                    .queryParam("customerId", savedCustomer.id.toString()),
            )
                .andExpect(jsonPath("$").isArray)
                .andExpect(content().string("[]"))
        }

        @Test
        fun `Given existent Customer ID, with 2 Credits, then return array with the 2 Credits`() {
            // given
            val savedCustomer = mockSavedCustomer(cpf = "215.792.190-71")
            mockSavedCredit(
                customerId = savedCustomer.id!!,
            )
            mockSavedCredit(
                customerId = savedCustomer.id!!,
            )

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/credits")
                    .queryParam("customerId", savedCustomer.id.toString()),
            )
                .andExpect(jsonPath("$").isArray)
                .andExpect(jsonPath("$.length()").value(2))
        }
    }

    @Nested
    inner class FindByCreditCode {
        @Test
        fun `Given non existent Credit Code, then return status 400`() {
            // then
            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/credits/${UUID.randomUUID()}")
                    .queryParam("customerId", faker.number().digits(8)),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun `Given existent Credit Code, but non existent Customer ID, then return status 400`() {
            // given
            val savedCustomer = mockSavedCustomer(cpf = "570.971.980-36")
            val savedCredit = mockSavedCredit(customerId = savedCustomer.id!!)

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/credits/${savedCredit.creditCode}")
                    .queryParam("customerId", faker.number().digits(8)),
            )
                .andExpect(status().isBadRequest)
        }

        @Test
        fun `Given existent Credit Code and Customer ID, then return status 200`() {
            // given
            val savedCustomer = mockSavedCustomer(cpf = "505.976.850-37")
            val savedCredit = mockSavedCredit(customerId = savedCustomer.id!!)

            // then
            mockMvc.perform(
                MockMvcRequestBuilders.get("/api/credits/${savedCredit.creditCode}")
                    .queryParam("customerId", savedCustomer.id.toString()),
            )
                .andExpect(status().isOk)
        }
    }

    private fun mockCreditDto(
        dayFirstOfInstallment: LocalDate = LocalDate.now().plusDays(1),
        numberOfInstallments: Int = faker.number().numberBetween(1, 48),
        customerId: Long,
    ) = CreditDto(
        creditValue = BigDecimal(faker.number().randomDouble(2, 1_000, 20_000)),
        dayFirstOfInstallment = dayFirstOfInstallment,
        numberOfInstallments = numberOfInstallments,
        customerId = customerId,
    )

    private fun mockSavedCredit(
        dayFirstOfInstallment: LocalDate = LocalDate.now().plusDays(1),
        numberOfInstallments: Int = faker.number().numberBetween(1, 48),
        customerId: Long,
    ) = creditRepository.save(
        this.mockCreditDto(
            dayFirstOfInstallment = dayFirstOfInstallment,
            numberOfInstallments = numberOfInstallments,
            customerId = customerId,
        ).toEntity(),
    )

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

    private fun mockSavedCustomer(cpf: String) = customerRepository.save(
        this.mockCustomerDto(
            cpf = cpf,
        ).toEntity(),
    )
}
