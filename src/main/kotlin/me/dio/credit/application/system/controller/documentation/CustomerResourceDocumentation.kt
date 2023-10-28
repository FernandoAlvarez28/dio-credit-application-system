package me.dio.credit.application.system.controller.documentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import me.dio.credit.application.system.dto.request.CustomerDto
import me.dio.credit.application.system.dto.request.CustomerUpdateDto
import me.dio.credit.application.system.dto.response.CustomerView
import me.dio.credit.application.system.exception.ExceptionDetails
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@Tag(name = "Customer")
interface CustomerResourceDocumentation {

    @Operation(
        summary = "Register Customer",
    )
    @ApiResponse(
        responseCode = "201",
        description = "The saved Customer",
    )
    fun saveCustomer(
        @RequestBody @Valid
        customerDto: CustomerDto,
    ): ResponseEntity<CustomerView>

    @Operation(
        summary = "Return Customer by ID",
    )
    @ApiResponse(
        responseCode = "200",
        description = "The Customer",
    )
    @ApiResponse(
        responseCode = "400",
        description = "If the Customer is not found",
        content = [
            Content(
                schema = Schema(implementation = ExceptionDetails::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE,
            ),
        ],
    )
    fun findById(
        @PathVariable id: Long,
    ): ResponseEntity<CustomerView>

    @Operation(
        summary = "Delete a Customer by ID",
    )
    @ApiResponse(
        responseCode = "204",
        description = "Nothing",
    )
    @ApiResponse(
        responseCode = "400",
        description = "If the Customer is not found",
        content = [
            Content(
                schema = Schema(implementation = ExceptionDetails::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE,
            ),
        ],
    )
    fun deleteCustomer(
        @PathVariable id: Long,
    )

    @Operation(
        summary = "Update a Customer by ID",
    )
    @ApiResponse(
        responseCode = "200",
        description = "The updated Customer",
    )
    @ApiResponse(
        responseCode = "400",
        description = "If the Customer is not found",
        content = [
            Content(
                schema = Schema(implementation = ExceptionDetails::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE,
            ),
        ],
    )
    fun updateCustomer(
        @RequestParam(value = "customerId")
        id: Long,
        @RequestBody @Valid
        customerUpdateDto: CustomerUpdateDto,
    ): ResponseEntity<CustomerView>
}
