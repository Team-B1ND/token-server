package b1nd.tokenserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class TokenServerApplication

fun main(args: Array<String>) {
	runApplication<TokenServerApplication>(*args)
}
