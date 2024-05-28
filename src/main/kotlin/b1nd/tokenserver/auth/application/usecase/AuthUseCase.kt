package b1nd.tokenserver.auth.application.usecase

import b1nd.tokenserver.auth.application.outport.TokenPort
import b1nd.tokenserver.auth.application.usecase.data.*
import b1nd.tokenserver.auth.domain.model.JWTType
import b1nd.tokenserver.auth.domain.model.Token
import b1nd.tokenserver.auth.domain.model.WrongTokenTypeException
import org.springframework.stereotype.Component

@Component
class AuthUseCase(val tokenPort: TokenPort) {

    fun issueAccessToken(req: IssueTokenRequest): TokenResponse {
        return issueToken(req, JWTType.ACCESS)
    }

    fun reissueAccessToken(req: ReissueAccessTokenRequest): TokenResponse {
        val token: Token = tokenPort.parse(req.refreshToken, JWTType.REFRESH)
        if(token.isNotRefreshToken()) {
            throw WrongTokenTypeException
        }
        return issueToken(IssueTokenRequest(token.subject, token.role), JWTType.ACCESS)
    }

    fun issueRefreshToken(req: IssueTokenRequest): TokenResponse {
        return issueToken(req, JWTType.REFRESH)
    }

    private fun issueToken(req: IssueTokenRequest, type: JWTType): TokenResponse {
        return TokenResponse(
            tokenPort.issue(req.memberId, req.accessLevel, type)
        )
    }

    fun verifyToken(req: VerifyTokenRequest): VerifyTokenResponse {
        val token: Token = tokenPort.parse(req.token, JWTType.ACCESS)
        return VerifyTokenResponse(token.subject, token.role, 0)
    }

}
