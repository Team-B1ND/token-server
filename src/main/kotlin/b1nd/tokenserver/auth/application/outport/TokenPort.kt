package b1nd.tokenserver.auth.application.outport

import b1nd.tokenserver.auth.domain.model.JWTType
import b1nd.tokenserver.auth.domain.model.Token

interface TokenPort {

    fun issue(subject: String, role: Int, type: JWTType): String

    fun parse(token: String, type: JWTType): Token

}
