package b1nd.tokenserver.domain.auth

data class Token(
    val memberId: String,
    val accessLevel: Int,
    val subject: TokenType
) {

    fun isNotRefreshToken(): Boolean {
        return TokenType.REFRESH != subject
    }

}
