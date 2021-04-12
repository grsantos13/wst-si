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
    private val exporterClient: ExporterServiceGrpc.ExporterServiceBlockingStub,
    private val service: SiService
) : SendShippingInstructionsServiceGrpc.SendShippingInstructionsServiceImplBase() {

    @Transactional
    override fun send(
        request: ShippingInstructionsRequest,
        responseObserver: StreamObserver<ShippingInstructionsResponse>
    ) {
        val order = readOrderClient.read(
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
        ).ordersList[0]

        val exporter = exporterClient.read(
            ReadExporterRequest.newBuilder()
                .setCode(order.exporter.code)
                .build()
        ).exportersList[0]

        val instructions = order.toModel(request, exporter)
        service.persist(instructions)

        MailSender.run {
            send(
                instructions,
                request.manufacturerIsExporter,
                request.toList
            )
        }

        responseObserver.onNext(ShippingInstructionsResponse.newBuilder().setSent(true).build())
        responseObserver.onCompleted()
    }
}
