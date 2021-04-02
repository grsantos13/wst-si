package br.com.gn.si

import br.com.gn.shared.exception.SIAlreadySentException
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

    fun existsByOrderNumber(@NotBlank @Size(min = 10) orderNumber: String): Boolean {
        val query = manager.createQuery(" select 1 from ShippingInstructions where orderNumber = :number ")
        query.setParameter("number", orderNumber)

        return query.resultList.isNotEmpty()
    }
}