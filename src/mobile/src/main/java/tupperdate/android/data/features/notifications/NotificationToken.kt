package tupperdate.android.data.features.notifications

/**
 * A token that can be used to send messages uniquely to this device. Generally, this will be
 * done when the server wants to notify this particular device that some specific content has
 * changed (for instance, a new chat message might have been sent).
 *
 * @param value the unique token that lets the server access this device.
 */
data class NotificationToken(
    val value: String,
)
