package b1nd.tokenserver.domain.auth.exception

import b1nd.tokenserver.domain.common.exception.BaseException

object EmptyTokenException : BaseException(
    400, "토큰이 전송되지 않았습니다"
)

object InvalidTokenException : BaseException(
    401, "위조된 토큰입니다"
)

object WrongTokenTypeException : BaseException(
    401, "잘못된 토큰 타입입니다"
)

object ExpiredTokenException : BaseException(
    410, "만료된 토큰입니다"
)
