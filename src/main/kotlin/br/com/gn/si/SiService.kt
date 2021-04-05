package br.com.gn.si

import br.com.gn.shared.exception.ObjectNotFoundException
import br.com.gn.shared.exception.SIAlreadySentException
import br.com.gn.si.send.MailSender
import io.micronaut.validation.Validated
import javax.inject.Singleton
import javax.persistence.EntityManager
import javax.validation.Valid
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Validated
@Singleton
class SiService(
    private val manager: EntityManager
) {

    fun persist(@NotNull @Valid si: ShippingInstructions): ShippingInstructions {
        if (existsByOrderNumber(si.orderNumber))
            throw SIAlreadySentException("SI for order ${si.orderNumber} has already been sent")

        manager.persist(si)
        return si;
    }

    fun find(@NotBlank @Size(min = 10) order: String): String {
        val query = manager.createQuery(
            " select si from ShippingInstructions si where orderNumber = :number ",
            ShippingInstructions::class.java
        )
        query.setParameter("number", order)
        val resultList = query.resultList

        val si = when {
            resultList.isEmpty() -> throw ObjectNotFoundException("Shipping instructions not found for order $order")
            else -> resultList[0]
        }

        return MailSender.message(
            si = si,
            exporterIsManufacturer = si.manufacturer.manufacturerName == si.exporter.exporterName
        )
    }

    fun existsByOrderNumber(@NotBlank @Size(min = 10) orderNumber: String): Boolean {
        val query = manager.createQuery(" select 1 from ShippingInstructions where orderNumber = :number ")
        query.setParameter("number", orderNumber)

        return query.resultList.isNotEmpty()
    }
}