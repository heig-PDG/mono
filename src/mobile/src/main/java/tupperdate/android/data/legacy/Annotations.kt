package tupperdate.android.data.legacy

import kotlin.RequiresOptIn.Level.WARNING

/**
 * An annotation that is put on elements of the outdated Tupperdate api, and that are expected to
 * break sometime soon.
 *
 * If you use one of these APIs, please consider upgrading it rather than using the obsolete
 * version !
 */
@RequiresOptIn(
    message = "This API should be migrated to the new Koin + Room + ViewModel architecture.",
    level = WARNING, // set this as ERROR to spot missing migrations.
)
annotation class ObsoleteTupperdateApi
