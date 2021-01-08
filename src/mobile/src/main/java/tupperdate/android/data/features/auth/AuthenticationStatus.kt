package tupperdate.android.data.features.auth

sealed class AuthenticationStatus {

    object NoAuthentication : AuthenticationStatus()

    interface Connected {
        val identifier: String
        val phoneNumber: String
        val token: String
    }

    data class NoProfile(
        override val identifier: String,
        override val phoneNumber: String,
        override val token: String,
    ) : AuthenticationStatus(), Connected

    data class Profile(
        override val identifier: String,
        override val phoneNumber: String,
        override val token: String,
        val displayName: String,
        val displayPictureUrl: String?,
    ) : AuthenticationStatus(), Connected
}
