package tupperdate.web.facade.profiles

import tupperdate.common.dto.NewNotificationTokenDTO
import tupperdate.web.model.profiles.ModelNotificationToken

data class NewNotificationToken(
    val token: String,
)

fun NewNotificationTokenDTO.toNotificationToken(): NewNotificationToken {
    return NewNotificationToken(token)
}

fun NewNotificationToken.toModelToken(): ModelNotificationToken {
    return ModelNotificationToken(token)
}
