package b1nd.tokenserver.domain.auth.outport

import b1nd.tokenserver.domain.auth.core.JWTType
import b1nd.tokenserver.domain.auth.core.Token

interface TokenPort {

    fun issue(memberId: String, accessLevel: Int, type: JWTType): String

    fun parse(token: String, type: JWTType): Token

}
