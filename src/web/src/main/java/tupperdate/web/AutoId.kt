package tupperdate.web

import kotlin.random.Random

fun autoId(): String {
    // Alphanumeric characters
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";

    var autoId = "";
    val targetLength = 20;
    while (autoId.length < targetLength) {
        val rand = Random.nextInt(chars.length)
        autoId += chars[rand]
    }

    return autoId;
}
