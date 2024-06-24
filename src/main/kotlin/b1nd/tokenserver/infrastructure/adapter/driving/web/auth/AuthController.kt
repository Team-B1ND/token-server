package b1nd.tokenserver.infrastructure.adapter.driving.web.auth

import b1nd.tokenserver.application.auth.usecase.AuthUseCase
import b1nd.tokenserver.application.auth.usecase.data.*
import b1nd.tokenserver.application.common.data.response.BaseResponse
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
        return useCase.issueAccessToken(request)
    }

    @PostMapping("/token/access/reissue")
    fun reissueAccessToken(@RequestBody request: ReissueAccessTokenRequest): Mono<BaseResponse<TokenResponse>> {
        return useCase.reissueAccessToken(request)
    }

    @PostMapping("/token/refresh")
    fun issueRefreshToken(@RequestBody request: IssueTokenRequest): Mono<BaseResponse<TokenResponse>> {
        return useCase.issueRefreshToken(request)
    }

    @PostMapping("/token/verify")
    fun verifyToken(@RequestBody request: VerifyTokenRequest): Mono<BaseResponse<VerifyTokenResponse>> {
        return useCase.verifyToken(request)
    }

}
