package b1nd.tokenserver.infrastructure.global.data.response

data class BaseResponse<T>(val status: Int, val message: String, val data: T)
