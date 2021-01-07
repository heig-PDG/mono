package tupperdate.android.data.features.auth

sealed class AuthenticationStatus {

    /**
     * An interface that's implemented by all the authentication statuses that uniquely identify a
     * user, and have a notion of current user.
     */
    interface Identified {
        val identifier: String
    }

    /**
     * An interface that's implemented by authentication statuses that can interact with the server,
     * and have a token that can be used to decorate requests.
     */
    interface Connected : Identified {
        val token: String
    }

    /**
     * An interface that's implemented by all the authentication statuses that can be displayed on
     * the screen with the profile information.
     */
    interface Displayable : Identified {
        val phoneNumber: String
        val displayName: String
        val displayPictureUrl: String?
    }

    /**
     * An interface that defines an [AuthenticationStatus] that has loaded the remote profile of the
     * user, if available.
     */
    interface Loaded

    // Actual cases.

    /**
     * Loading some stuff. We can't tell anything about our state yet.
     */
    object Unknown : AuthenticationStatus()

    /**
     * The user is not authenticated at all. Display the onboarding flow.
     */
    object None : AuthenticationStatus()

    /**
     * The application is still loading the user's information. The user should not be redirected to
     * the onboarding flow.
     */
    data class LoadingProfile(
        override val identifier: String,
    ) : AuthenticationStatus(), Identified

    /**
     * The user profile is absent, and they may edit it.
     */
    data class AbsentProfile(
        override val identifier: String,
        override val token: String,
    ) : AuthenticationStatus(), Loaded, Identified, Connected

    /**
     * The user profile is complete, and they can be displayed.
     */
    data class CompleteProfile(
        override val identifier: String,
        override val phoneNumber: String,
        override val token: String,
        override val displayName: String,
        override val displayPictureUrl: String?,
    ) : AuthenticationStatus(), Loaded, Connected, Displayable
}
