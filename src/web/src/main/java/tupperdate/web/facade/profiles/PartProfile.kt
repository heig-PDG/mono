package tupperdate.web.facade.profiles

import tupperdate.common.dto.MyUserPartDTO
import tupperdate.web.facade.PictureBase64

data class PartProfile(
    val displayName: String?,
    val displayNameProvided: Boolean,
    val imageBase64: PictureBase64?,
    val imageBase64Provided: Boolean,
)

fun MyUserPartDTO.toPartProfile(): PartProfile {
    val displayNamePair = when(val opt = displayName) {
        is utils.OptionalProperty.Provided -> opt.value to true
        utils.OptionalProperty.NotProvided -> null to false
    }
    val imageBase64Pair = when(val opt = imageBase64) {
        is utils.OptionalProperty.Provided -> opt.value to true
        utils.OptionalProperty.NotProvided -> null to false
    }

    return PartProfile(
        displayName = displayNamePair.first,
        displayNameProvided = displayNamePair.second,
        imageBase64 = imageBase64Pair.first?.let { PictureBase64(it) },
        imageBase64Provided = imageBase64Pair.second,
    )
}
