package tupperdate.common.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import utils.OptionalProperty
import utils.OptionalPropertySerializer
import utils.TimestampSerializer
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class RecipePartDTO(
    @Serializable(with = OptionalPropertySerializer::class)
    val title: OptionalProperty<String?> = OptionalProperty.NotProvided,
    @Serializable(with = OptionalPropertySerializer::class)
    val description: OptionalProperty<String?> = OptionalProperty.NotProvided,
    @Serializable(with = OptionalPropertySerializer::class)
    val picture: OptionalProperty<String?> = OptionalProperty.NotProvided,
    val attributes: RecipeAttributesPartDTO,
)
