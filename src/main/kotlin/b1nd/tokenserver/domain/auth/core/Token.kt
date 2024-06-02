package b1nd.tokenserver.domain.auth.core

data class Token(
    val memberId: String,
    val accessLevel: Int,
    val subject: JWTType
) {

    fun isNotRefreshToken(): Boolean {
        return JWTType.REFRESH != subject
    }

}

enum class JWTType {

    ACCESS, REFRESH;

    companion object {
        fun of(value: String): JWTType {
            if("token" == value) {
                return ACCESS
            }
            if("refreshToken" == value) {
                return REFRESH
            }

            return JWTType.valueOf(value)
        }
    }

}
