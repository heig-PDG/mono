package utils

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

sealed class OptionalProperty<T> {
    object NotProvided : OptionalProperty<Nothing>()
    data class Provided<T>(val value: T) : OptionalProperty<T>()
}

open class OptionalPropertySerializer<T>(
    private val valueSerializer: KSerializer<T>
) : KSerializer<OptionalProperty<T>> {

    override val descriptor = valueSerializer.descriptor

    override fun deserialize(
        decoder: Decoder,
    ) = OptionalProperty.Provided(
        valueSerializer.deserialize(decoder),
    )

    override fun serialize(
        encoder: Encoder,
        value: OptionalProperty<T>,
    ) {
        when (value) {
            OptionalProperty.NotProvided -> throw SerializationException(
                "Tried to serialize an optional property that had no value present." +
                        " Is encodeDefaults false?"
            )
            is OptionalProperty.Provided ->
                valueSerializer.serialize(encoder, value.value)
        }
    }
}
