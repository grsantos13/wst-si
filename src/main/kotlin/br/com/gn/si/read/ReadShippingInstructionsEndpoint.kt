package br.com.gn.si.read

import br.com.gn.ReadShippingInstructionsServiceGrpc
import br.com.gn.SentShippingInstructionsRequest
import br.com.gn.ShippingInstructionsPageResponse
import br.com.gn.ShippingInstructionsResponse
import br.com.gn.shared.exception.ErrorHandler
import br.com.gn.si.SiService
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.transaction.Transactional

@ErrorHandler
@Singleton
open class ReadShippingInstructionsEndpoint(
    private val service: SiService
) : ReadShippingInstructionsServiceGrpc.ReadShippingInstructionsServiceImplBase() {

    @Transactional
    override fun sent(
        request: SentShippingInstructionsRequest,
        responseObserver: StreamObserver<ShippingInstructionsResponse>
    ) {
        if (request.orderNumber.isNullOrBlank())
            throw IllegalArgumentException("Order number must not be blank")

        val response = ShippingInstructionsResponse.newBuilder()
            .setSent(service.existsByOrderNumber(request.orderNumber))
            .build()

        responseObserver.onNext(response)
        responseObserver.onCompleted()
    }

    @Transactional
    override fun show(
        request: SentShippingInstructionsRequest,
        responseObserver: StreamObserver<ShippingInstructionsPageResponse>
    ) {
        if (request.orderNumber.isNullOrBlank())
            throw IllegalArgumentException("Order number must not be blank")

        var si = service.find(request.orderNumber)
        si = si.replace("\n", "")
        si = si.replace("\"", "'")

        responseObserver.onNext(
            ShippingInstructionsPageResponse.newBuilder()
                .setPage(si)
                .build()
        )
        responseObserver.onCompleted()
    }
}