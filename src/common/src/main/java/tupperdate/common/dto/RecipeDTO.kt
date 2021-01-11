package tupperdate.common.dto

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Serializer
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import utils.TimestampSerializer
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalSerializationApi::class)
@Serializable
data class RecipeDTO(
    val id: String,
    val title: String,
    val description: String?,
    @Serializable(with = TimestampSerializer::class)
    val timestamp: Long,
    val picture: String?,
    val attributes: RecipeAttributesDTO,
)
