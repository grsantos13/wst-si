package br.com.gn.si

import javax.persistence.Column
import javax.persistence.Embeddable
import javax.validation.constraints.NotNull

@Embeddable
class Manufacturer(
    @Column(updatable = false) val manufacturerName: String? = null,
    @Column(updatable = false) val manufacturerStreet: String? = null,
    @Column(updatable = false) val manufacturerCityAndZipCode: String? = null,
    @Column(updatable = false) val manufacturerCountry: String? = null,
    @field:NotNull @Column(updatable = false, nullable = false) val manufacturerConfirmed: Boolean
)
