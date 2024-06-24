package b1nd.tokenserver.application.auth.usecase.data

data class VerifyTokenRequest(val token: String)

data class IssueTokenRequest(val memberId: String, val accessLevel: Int)

data class ReissueAccessTokenRequest(val refreshToken: String)
