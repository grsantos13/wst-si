package br.com.gn.shared.exception.handlers

import br.com.gn.shared.exception.ExceptionHandler
import br.com.gn.shared.exception.SIAlreadySentException
import io.grpc.Status
import javax.inject.Singleton

@Singleton
class ObjectAlreadyExistsExceptionHandler : ExceptionHandler<SIAlreadySentException> {
    override fun handle(e: SIAlreadySentException): ExceptionHandler.StatusWrapper {
        return ExceptionHandler.StatusWrapper(
            Status.ALREADY_EXISTS
                .withDescription(e.message)
                .withCause(e)
        )
    }

    override fun supports(e: Exception): Boolean {
        return e is SIAlreadySentException
    }
}