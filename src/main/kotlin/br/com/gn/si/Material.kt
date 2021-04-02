package br.com.gn.si

import java.math.BigDecimal
import javax.persistence.*
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Pattern

@Entity
class Material(
    @field:NotBlank @Column(nullable = false, updatable = false) val code: String,
    @field:NotBlank @Column(nullable = false, updatable = false) val description: String,
    @field:NotNull @Column(nullable = false, updatable = false) val quantity: BigDecimal,
    @field:NotNull @Column(nullable = false, updatable = false) val unitPrice: BigDecimal,
    @field:NotNull @field:Pattern(regexp = "[0-9]{8}") @Column(nullable = false, updatable = false) val ncm: String,
    @field:NotNull @field:Valid @ManyToOne val shippingInstructions: ShippingInstructions
) {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

}
