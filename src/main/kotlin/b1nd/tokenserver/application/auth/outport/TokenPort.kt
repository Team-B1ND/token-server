package b1nd.tokenserver.application.auth.outport

import b1nd.tokenserver.domain.auth.TokenType
import b1nd.tokenserver.domain.auth.Token

interface TokenPort {

    fun issue(memberId: String, accessLevel: Int, type: TokenType): String

    fun parse(token: String, type: TokenType): Token

}
