package b1nd.tokenserver.application.common.async

import reactor.core.publisher.Mono
import reactor.core.scheduler.Schedulers
import java.util.concurrent.Callable

class AsyncCPUBoundTask {

    companion object {

        fun <T> execute(supplier: Callable<T>): Mono<T> {
            return Mono.fromCallable(supplier)
                .subscribeOn(Schedulers.boundedElastic())
        }

    }

}
