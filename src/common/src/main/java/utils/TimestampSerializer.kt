package utils

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.text.SimpleDateFormat
import java.util.*

/**
 * A [KSerializer] that transforms timestamps to UNIX timestamps.
 */
@ExperimentalSerializationApi
@Serializer(forClass = Long::class)
object TimestampSerializer : KSerializer<Long> {

    @Suppress("SimpleDateFormat")
    private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

    override fun serialize(encoder: Encoder, value: Long) {
        encoder.encodeString(format.format(Date(value)))
    }

    override fun deserialize(decoder: Decoder): Long {
        return format.parse(decoder.decodeString())?.time ?: 0L
    }
}
