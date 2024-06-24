package b1nd.tokenserver.domain.auth

enum class TokenType {

    ACCESS, REFRESH;

    companion object {
        fun of(value: String): TokenType {
            if("token" == value) {
                return ACCESS
            }
            if("refreshToken" == value) {
                return REFRESH
            }

            return TokenType.valueOf(value)
        }
    }

}
