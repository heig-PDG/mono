package tupperdate.web.utils

import io.ktor.http.*
import io.ktor.server.testing.*

const val botId = "botId"
const val botName = "botName"

fun TestApplicationRequest.authRequest(id: String) {
    addHeader(HttpHeaders.Authorization, "Bearer $id")
}

fun TestApplicationRequest.jsonType() {
    addHeader("Content-Type", "application/json")
}
