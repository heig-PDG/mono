package tupperdate.web.utils

import io.ktor.http.*
import io.ktor.server.testing.*

const val token = "Bearer Tokythetoken"
const val userId = "botId"
const val botName = "botName"

fun TestApplicationRequest.authRequest() {
    addHeader(HttpHeaders.Authorization, token)
}

fun TestApplicationRequest.jsonType() {
    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.contentType)
}
