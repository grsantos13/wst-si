package br.com.gn.si.read

import br.com.gn.ReadShippingInstructionsServiceGrpc
import br.com.gn.SentShippingInstructionsRequest
import br.com.gn.ShippingInstructionsResponse
import br.com.gn.si.SiService
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.transaction.Transactional

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
}