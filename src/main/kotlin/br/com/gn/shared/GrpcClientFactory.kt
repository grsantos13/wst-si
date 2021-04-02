package br.com.gn.shared

import br.com.gn.ReadOrderServiceGrpc
import io.grpc.ManagedChannel
import io.micronaut.context.annotation.Factory
import io.micronaut.grpc.annotation.GrpcChannel
import javax.inject.Singleton

@Factory
class GrpcClientFactory {

    @Singleton
    fun wstMainReadClientStub(@GrpcChannel("wstMainRead") channel: ManagedChannel) =
        ReadOrderServiceGrpc.newBlockingStub(channel)
}