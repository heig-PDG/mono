package tupperdate.web.utils

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

/**
 * Extension method for configuring [StatusPages] that encapsulates the functionality of catching
 * the [StatusException] and actually responding the Http status code.
 */
fun StatusPages.Configuration.registerException() {
    exception<StatusException> { exception ->
        call.respond(exception.code)
    }
}

/**
 * Global function that throws a [StatusException], to be catched by the [StatusPages] feature to respond the Http status code
 */
fun statusException(code: HttpStatusCode): Nothing = throw StatusException(code)

/**
 * Exception used to be captured by [StatusPages] to perform a redirect.
 */
class StatusException(val code: HttpStatusCode) : Exception()
