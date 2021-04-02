package br.com.gn.si.send

import br.com.gn.*
import br.com.gn.shared.exception.ErrorHandler
import br.com.gn.si.SiService
import br.com.gn.si.toModel
import io.grpc.stub.StreamObserver
import javax.inject.Singleton
import javax.transaction.Transactional

@ErrorHandler
@Singleton
class SendShippingInstructionsEndpoint(
    private val readOrderClient: ReadOrderServiceGrpc.ReadOrderServiceBlockingStub,
    private val service: SiService
) : SendShippingInstructionsServiceGrpc.SendShippingInstructionsServiceImplBase() {

    @Transactional
    override fun send(
        request: ShippingInstructionsRequest,
        responseObserver: StreamObserver<ShippingInstructionsResponse>
    ) {
        val response: OrdersResponse = readOrderClient.read(
            ReadOrderRequest.newBuilder()
                .setNumber(request.orderNumber)
                .setPageable(
                    Pageable.newBuilder()
                        .setDirection(OrderDirection.ASC)
                        .setOrderBy("number")
                        .setPage(0)
                        .setSize(25)
                        .build()
                )
                .build()
        )

        val instructions = response.ordersList[0].toModel(request)
        service.persist(instructions)

        MailSender.run {
            send(
                subject = "SI",
                instructions,
                request.manufacturerIsExporter,
                "grodrigueset@gmail.com"
            )
        }

        responseObserver.onNext(ShippingInstructionsResponse.newBuilder().setSent(true).build())
        responseObserver.onCompleted()
    }
}
