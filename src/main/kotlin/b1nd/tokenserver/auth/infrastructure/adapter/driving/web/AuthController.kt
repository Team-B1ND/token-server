package b1nd.tokenserver.auth.infrastructure.adapter.driving.web

import b1nd.tokenserver.auth.application.usecase.AuthUseCase
import b1nd.tokenserver.auth.application.usecase.data.*
import b1nd.tokenserver.global.data.response.BaseResponse
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono

@RestController
@RequestMapping("/auth")
class AuthController(val useCase: AuthUseCase) {

    @PostMapping("/token/access")
    fun issueAccessToken(@RequestBody request: IssueTokenRequest): Mono<BaseResponse<TokenResponse>> {
        val response: TokenResponse = useCase.issueAccessToken(request)
        return Mono.just(BaseResponse(200, "Access 토큰 발급 성공", response))
    }

    @PostMapping("/token/access/reissue")
    fun reissueAccessToken(@RequestBody request: ReissueAccessTokenRequest): Mono<BaseResponse<TokenResponse>> {
        val response: TokenResponse = useCase.reissueAccessToken(request)
        return Mono.just(BaseResponse(200, "Access 토큰 재발급 성공", response))
    }

    @PostMapping("/token/refresh")
    fun issueRefreshToken(@RequestBody request: IssueTokenRequest): Mono<BaseResponse<TokenResponse>> {
        val response: TokenResponse = useCase.issueRefreshToken(request)
        return Mono.just(BaseResponse(200, "Refresh 토큰 발급 성공", response))
    }

    @PostMapping("/token/verify")
    fun verifyToken(@RequestBody request: VerifyTokenRequest): Mono<BaseResponse<VerifyTokenResponse>> {
        val response: VerifyTokenResponse = useCase.verifyToken(request)
        return Mono.just(BaseResponse(200, "토큰 검증 성공", response))
    }

}
