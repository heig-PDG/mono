package tupperdate.common.dto

import kotlinx.serialization.Serializable
import utils.OptionalProperty
import utils.OptionalPropertySerializer

@Serializable
data class MyUserPartDTO(
    @Serializable(with = OptionalPropertySerializer::class)
    val displayName: OptionalProperty<String?>,
    @Serializable(with = OptionalPropertySerializer::class)
    val imageBase64: OptionalProperty<String?>,
)

