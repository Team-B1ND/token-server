package b1nd.tokenserver.domain.auth.usecase

import b1nd.tokenserver.domain.auth.outport.TokenPort
import b1nd.tokenserver.domain.auth.core.JWTType
import b1nd.tokenserver.domain.auth.core.Token
import b1nd.tokenserver.domain.auth.core.exception.WrongTokenTypeException
import b1nd.tokenserver.domain.auth.usecase.data.*
import org.springframework.stereotype.Component

@Component
class AuthUseCase(val tokenPort: TokenPort) {

    fun issueAccessToken(req: IssueTokenRequest): TokenResponse {
        return issueToken(req, JWTType.ACCESS)
    }

    //todo Redis 조회
    fun reissueAccessToken(req: ReissueAccessTokenRequest): TokenResponse {
        val token: Token = tokenPort.parse(req.refreshToken, JWTType.REFRESH)
        if(token.isNotRefreshToken()) {
            throw WrongTokenTypeException
        }
        return issueToken(IssueTokenRequest(token.subject, token.role), JWTType.ACCESS)
    }

    //todo Redis 저장
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
