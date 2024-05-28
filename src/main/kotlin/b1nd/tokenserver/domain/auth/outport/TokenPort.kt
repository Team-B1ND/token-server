package b1nd.tokenserver.domain.auth.outport

import b1nd.tokenserver.domain.auth.core.JWTType
import b1nd.tokenserver.domain.auth.core.Token

interface TokenPort {

    fun issue(subject: String, role: Int, type: JWTType): String

    fun parse(token: String, type: JWTType): Token

}
