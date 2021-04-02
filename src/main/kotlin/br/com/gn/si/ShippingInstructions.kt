package br.com.gn.si

import br.com.gn.Incoterm
import java.time.LocalDate
import javax.persistence.*
import javax.persistence.CascadeType.PERSIST
import javax.persistence.CascadeType.REMOVE
import javax.persistence.EnumType.STRING
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class ShippingInstructions(
    @field:NotBlank @field:Size(min = 10) @Column(
        nullable = false,
        unique = true,
        updatable = false
    ) val orderNumber: String,
    @field:NotNull @field:Valid @Embedded val importer: Importer,
    @field:NotNull @field:Valid @Embedded val exporter: Exporter,
    @field:NotNull @field:Valid @Embedded val manufacturer: Manufacturer,
    materials: List<MaterialRequest>,
    @field:NotNull @Enumerated(STRING) @Column(nullable = false) val incoterm: Incoterm,
    @field:NotBlank @Column(nullable = false) val paymentTerms: String,
    @field:NotNull @Column(nullable = false) val preShipmentLicense: Boolean,
    @field:NotNull @Column(nullable = false) val modal: String,
    @field:NotBlank @Column(nullable = false) val freightForwarder: String,
    @field:NotBlank @Column(nullable = false) val originPort: String,
    @field:NotBlank @Column(nullable = false) val destinationPort: String,
    @field:NotBlank @Column(nullable = false) val responsible: String,
    @field:NotBlank @Column(nullable = false) val emails: String,
    @field:NotBlank @Column(nullable = false) val brokerReference: String,
    val availabilityRequest: LocalDate?,
    val departureRequest: LocalDate?,
    val arrivalRequest: LocalDate?,
    val deliveryRequest: LocalDate?,
) {
    @field:NotNull
    @field:Valid
    @OneToMany(mappedBy = "shippingInstructions", cascade = [PERSIST, REMOVE])
    val materials = materials.map {
        Material(
            code = it.code,
            description = it.description,
            quantity = it.quantity!!,
            unitPrice = it.unitPrice!!,
            ncm = it.ncm,
            shippingInstructions = this
        )
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null

}