package br.com.gn.si

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.validation.constraints.NotBlank

@Embeddable
class Exporter(
    @field:NotBlank @Column(nullable = false, updatable = false) val exporterName: String,
    @field:NotBlank @Column(nullable = false, updatable = false) val exporterStreet: String,
    @field:NotBlank @Column(nullable = false, updatable = false) val exporterCityAndZipCode: String,
    @field:NotBlank @Column(nullable = false, updatable = false) val exporterCountry: String,
    @field:NotBlank @Column(nullable = false, updatable = false) val exporterCurrency: String
)
