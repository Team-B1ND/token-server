package b1nd.tokenserver.application.common.data.response

data class BaseResponse<T>(val status: Int, val message: String, val data: T) {

    companion object {

        fun <T> ok(message: String, data: T): BaseResponse<T> {
            return BaseResponse(200, message, data)
        }

    }

}
