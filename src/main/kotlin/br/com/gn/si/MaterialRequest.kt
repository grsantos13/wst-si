package br.com.gn.si

import io.micronaut.core.annotation.Introspected
import java.math.BigDecimal
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@Introspected
data class MaterialRequest(
    @field:NotBlank val code: String,
    @field:NotBlank val description: String,
    @field:NotNull val quantity: BigDecimal?,
    @field:NotNull val unitPrice: BigDecimal?,
    @field:NotNull @field:Pattern(regexp = "[0-9]{8}") val ncm: String,
) {

    fun toModel(shippingInstructions: ShippingInstructions): Material {
        return Material(
            code, description, quantity!!, unitPrice!!, ncm, shippingInstructions
        )
    }
}
