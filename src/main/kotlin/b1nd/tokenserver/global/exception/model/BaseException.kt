package b1nd.tokenserver.global.exception.model

open class BaseException(val status: Int, override val message: String) : RuntimeException()

object InternalServerException : BaseException(500, "Internal Server Error")
