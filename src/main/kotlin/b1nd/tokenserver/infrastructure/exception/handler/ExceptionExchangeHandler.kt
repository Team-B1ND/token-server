package b1nd.tokenserver.infrastructure.exception.handler

import b1nd.tokenserver.application.common.data.response.BaseErrorResponse
import b1nd.tokenserver.domain.common.exception.BaseException
import b1nd.tokenserver.domain.common.exception.InternalServerException
import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.autoconfigure.web.reactive.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.web.reactive.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.ErrorResponseException
import org.springframework.web.reactive.function.server.*
import reactor.core.publisher.Mono

@Component
@Order(-1)
class ExceptionExchangeHandler(
    errorAttributes: ErrorAttributes,
    webProperties: WebProperties,
    applicationContext: ApplicationContext,
    serverCodecConfigurer: ServerCodecConfigurer
) : AbstractErrorWebExceptionHandler(
    errorAttributes,
    webProperties.resources,
    applicationContext
) {

    private val log = LoggerFactory.getLogger(this::class.simpleName)

    init {
        super.setMessageReaders(serverCodecConfigurer.readers)
        super.setMessageWriters(serverCodecConfigurer.writers)
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes?): RouterFunction<ServerResponse> {
        return RouterFunctions.route(RequestPredicates.all(), this::handleError)
    }

    private fun handleError(request: ServerRequest): Mono<ServerResponse> =
        when(val e = super.getError(request)) {
            is BaseException -> buildErrorResponse(e)
            is ErrorResponseException -> buildErrorResponse(BaseException(e.statusCode.value(), e.message))
            else -> buildErrorResponse(InternalServerException)
        }

    private fun buildErrorResponse(e: BaseException): Mono<ServerResponse> {
        log.error(e.message)
        return ServerResponse.status(e.status).bodyValue(BaseErrorResponse(e.status, e.message))
    }

}
