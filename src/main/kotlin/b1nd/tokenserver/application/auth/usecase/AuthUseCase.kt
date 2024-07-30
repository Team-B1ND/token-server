package b1nd.tokenserver.application.auth.usecase

import b1nd.tokenserver.application.auth.outport.TokenPort
import b1nd.tokenserver.application.auth.usecase.data.*
import b1nd.tokenserver.domain.auth.TokenType
import b1nd.tokenserver.domain.auth.Token
import b1nd.tokenserver.domain.auth.exception.WrongTokenTypeException
import b1nd.tokenserver.application.common.data.response.BaseResponse
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthUseCase(val tokenPort: TokenPort) {

    fun issueAccessToken(request: IssueTokenRequest): Mono<BaseResponse<TokenResponse>> {
        return issueToken(request, TokenType.ACCESS)
            .map { data -> BaseResponse.ok("Access 토큰 발급 성공", data) }
    }

    fun reissueAccessToken(request: ReissueAccessTokenRequest): Mono<BaseResponse<TokenResponse>> {
        return Mono.fromCallable {
            val token: Token = tokenPort.parse(request.refreshToken, TokenType.REFRESH)
            if(token.isNotRefreshToken()) {
                throw WrongTokenTypeException
            }
            TokenResponse(tokenPort.issue(token.memberId, token.accessLevel, TokenType.ACCESS))
        }.map { data -> BaseResponse.ok("Access 토큰 재발급 성공", data) }
    }

    fun issueRefreshToken(request: IssueTokenRequest): Mono<BaseResponse<TokenResponse>> {
        return issueToken(request, TokenType.REFRESH)
            .map { data -> BaseResponse.ok("Refresh 토큰 발급 성공", data) }
    }

    private fun issueToken(request: IssueTokenRequest, type: TokenType): Mono<TokenResponse> {
        return Mono.fromCallable {
            TokenResponse(tokenPort.issue(request.memberId, request.accessLevel, type))
        }
    }

    fun verifyToken(request: VerifyTokenRequest): Mono<BaseResponse<VerifyTokenResponse>> {
        return Mono.fromCallable {
            val token: Token = tokenPort.parse(request.token, TokenType.ACCESS)
            VerifyTokenResponse(token.memberId, token.accessLevel, 0)
        }.map { info -> BaseResponse.ok("토큰 검증 성공", info) }
    }

}
