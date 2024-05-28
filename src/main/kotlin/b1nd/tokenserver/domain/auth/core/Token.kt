package b1nd.tokenserver.domain.auth.core

data class Token(
    val subject: String,
    val role: Int,
    val type: JWTType
) {

    fun isNotRefreshToken(): Boolean {
        return JWTType.REFRESH != type
    }

}

enum class JWTType { ACCESS, REFRESH }
