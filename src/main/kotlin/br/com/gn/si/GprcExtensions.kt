package br.com.gn.si

import br.com.gn.OrderResponse
import br.com.gn.PaymentTerms
import br.com.gn.ShippingInstructionsRequest
import br.com.gn.utils.toBigDecimal
import br.com.gn.utils.toLocalDate

fun OrderResponse.toModel(
    siRequest: ShippingInstructionsRequest
): ShippingInstructions {
    val manufacturer = when {
        siRequest.manufacturerIsExporter -> Manufacturer(
            manufacturerConfirmed = siRequest.manufacturerIsExporter,
            manufacturerName = exporter.name,
            manufacturerStreet = exporter.address.street,
            manufacturerCityAndZipCode = exporter.address.city + ", " + exporter.address.zipCode,
            manufacturerCountry = exporter.address.country
        )
        else -> Manufacturer(manufacturerConfirmed = siRequest.manufacturerIsExporter)
    }

    val availabilityRequest =
        necessity.toLocalDate()?.let {
            it.minusDays(exporter.totalLT.toLong())
                .plusDays(exporter.availabilityLT.toLong())
        }

    return ShippingInstructions(
        importer = Importer(
            importerFiscalName = importer.fiscalName,
            importerFiscalNumber = importer.fiscalNumber,
            importerCityAndCountry = importer.address.city + ", " + importer.address.country,
            importerStreetAndZipCode = importer.address.street + ", " + importer.address.zipCode
        ),
        exporter = Exporter(
            exporterName = exporter.name,
            exporterStreet = exporter.address.street,
            exporterCityAndZipCode = exporter.address.city + ", " + exporter.address.zipCode,
            exporterCountry = exporter.address.country,
            exporterCurrency = exporter.currency.name
        ),
        manufacturer = manufacturer,
        materials = itemsList.map {
            MaterialRequest(
                code = it.code,
                description = it.description,
                quantity = it.quantity.toBigDecimal(),
                unitPrice = it.unitPrice.toBigDecimal(),
                ncm = it.ncm
            )
        },
        incoterm = exporter.incoterm,
        paymentTerms = exporter.paymentTerms.toTransformedString(),
        preShipmentLicense = itemsList.any { it.preShipmentLicense },
        modal = "BY ${modal.name}",
        freightForwarder = siRequest.freightForwarder,
        originPort = siRequest.originPort,
        destinationPort = siRequest.destinationPort,
        responsible = this.responsible.name,
        emails = this.responsible.email,
        orderNumber = number,
        availabilityRequest = availabilityRequest,
        departureRequest = availabilityRequest?.plusDays(exporter.departureLT.toLong()),
        arrivalRequest = availabilityRequest?.plusDays(exporter.departureLT.toLong())
            ?.plusDays(exporter.arrivalLT.toLong()),
        deliveryRequest = necessity.toLocalDate(),
        brokerReference = brokerReference
    )
}

fun PaymentTerms.toTransformedString(): String {
    return this.name.replace("E", "") + " Days"
}