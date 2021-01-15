package tupperdate.android.data.features.notifications

/**
 * A [NotificationRepository] is an interface that lets you perform stuff related to notification
 * management. In particular, it offers some facilities to link the current device to the remote
 * server, and handle incoming messages.
 */
interface NotificationRepository {

    /**
     * Links the [NotificationToken] provided to the device to the currently logged in user, if
     * there is actually any.
     *
     * @param token the [NotificationToken] for this device.
     */
    fun link(token: NotificationToken)

    /**
     * Handles the incoming [Notification] and performs data-related side-effects related to it,
     * including displaying content or updating some local stores.
     */
    fun handle(notification: Notification)
}
