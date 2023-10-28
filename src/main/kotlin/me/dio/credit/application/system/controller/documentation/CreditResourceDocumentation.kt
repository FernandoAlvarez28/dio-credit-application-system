package me.dio.credit.application.system.controller.documentation

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import me.dio.credit.application.system.dto.request.CreditDto
import me.dio.credit.application.system.dto.response.CreditView
import me.dio.credit.application.system.dto.response.CreditViewList
import me.dio.credit.application.system.exception.ExceptionDetails
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.util.UUID

@Tag(name = "Credit")
interface CreditResourceDocumentation {

    @Operation(
        summary = "Request Credit",
    )
    @ApiResponse(
        responseCode = "201",
        description = "The requested Credit",
    )
    @ApiResponse(
        responseCode = "400",
        description = "If the first installment day is later than 3 months or the customer ID is not found",
        content = [
            Content(
                schema = Schema(implementation = ExceptionDetails::class),
                mediaType = MediaType.APPLICATION_JSON_VALUE,
            ),
        ],
    )
    fun saveCredit(
        @RequestBody @Valid
        creditDto: CreditDto,
    ): ResponseEntity<CreditView>

    @Operation(
        summary = "Return all Credit requests by Customer",
    )
    @ApiResponse(
        responseCode = "200",
        description = "The requested Credits",
    )
    fun findAllByCustomerId(
        @RequestParam(value = "customerId") customerId: Long,
    ): ResponseEntity<List<CreditViewList>>

    @Operation(
        summary = "Return requested Credit by Code",
    )
    @ApiResponse(
        responseCode = "200",
        description = "The requested Credits",
    )
    fun findByCreditCode(
        @RequestParam(value = "customerId") customerId: Long,
        @PathVariable creditCode: UUID,
    ): ResponseEntity<CreditView>
}
