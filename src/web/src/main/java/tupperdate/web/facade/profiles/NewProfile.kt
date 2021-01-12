package tupperdate.web.facade.profiles

data class NewProfile<Picture>(
    val displayName: String,
    val picture: Picture?,
)
