package tupperdate.web.facade.profiles

inline class PictureBase64(val encoded: String)
inline class PictureUrl(val url: String)

data class Profile<Picture>(
    val displayName: String,
    val picture: Picture?,
)
