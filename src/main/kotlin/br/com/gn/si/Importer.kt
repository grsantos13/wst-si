package br.com.gn.si

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank

@Embeddable
class Importer(
    @field:NotBlank @Column(nullable = false, updatable = false) val importerFiscalName: String,
    @field:NotBlank @Column(nullable = false, updatable = false) val importerFiscalNumber: String,
    @field:NotBlank @Column(nullable = false, updatable = false) val importerStreetAndZipCode: String,
    @field:NotBlank @Column(nullable = false, updatable = false) val importerCityAndCountry: String
)
