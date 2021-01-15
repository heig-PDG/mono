package tupperdate.web.utils

import io.ktor.http.*
import io.ktor.server.testing.*

const val token = "Bearer Tokythetoken"
const val botId = "botId"
const val botName = "botName"

fun TestApplicationRequest.authRequest() {
    addHeader(HttpHeaders.Authorization, token)
}

fun TestApplicationRequest.jsonType() {
    addHeader("Content-Type", "application/json")
}
