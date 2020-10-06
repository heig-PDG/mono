@file:JvmName("Main")

package tupperdate.web

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import tupperdate.common.helloWorld

private const val DefaultPort = 1234
private const val DefaultPortEnvVariable = "PORT"

@JvmName("main")
fun main() {
    val port = System.getenv(DefaultPortEnvVariable)?.toIntOrNull() ?: DefaultPort

    val server = embeddedServer(Netty, port = port) {
        install(DefaultHeaders)
        install(CallLogging)
        install(Routing) {
            get("/") {
                call.respondText(helloWorld())
            }
        }
    }

    server.start(wait = true)
}