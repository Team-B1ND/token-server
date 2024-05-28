package b1nd.tokenserver.global.data.response

data class BaseResponse<T>(val status: Int, val message: String, val data: T)
