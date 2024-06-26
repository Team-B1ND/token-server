package b1nd.tokenserver.application.auth.usecase.data

data class TokenResponse(
    val token: String
)

data class VerifyTokenResponse(
    val memberId: String,
    val accessLevel: Int,
    val apiKeyAccessLevel: Int
)
