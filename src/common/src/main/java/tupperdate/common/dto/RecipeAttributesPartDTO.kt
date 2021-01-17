package tupperdate.common.dto

import kotlinx.serialization.Serializable
import utils.OptionalProperty
import utils.OptionalPropertySerializer

@Serializable
data class RecipeAttributesPartDTO(
    @Serializable(with = OptionalPropertySerializer::class)
    val hasAllergens: OptionalProperty<Boolean> = OptionalProperty.NotProvided,
    @Serializable(with = OptionalPropertySerializer::class)
    val vegetarian: OptionalProperty<Boolean> = OptionalProperty.NotProvided,
    @Serializable(with = OptionalPropertySerializer::class)
    val warm: OptionalProperty<Boolean> = OptionalProperty.NotProvided,
)
