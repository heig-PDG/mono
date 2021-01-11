package tupperdate.android.data

import kotlin.RequiresOptIn.Level.ERROR

/**
 * An annotation that marks internal APIs for data management. These APIs should not be depended on
 * by code that resides outside of the [tupperdate.android.data] module.
 *
 * Usually, you'll want to annotation database and API calls with this annotation.
 */
@MustBeDocumented
@RequiresOptIn(level = ERROR)
annotation class InternalDataApi

/**
 * An annotation that marks implementations that require to be passed as parameters, rather than
 * being build directly through the DI framework.
 */
@RequiresOptIn(level = ERROR)
annotation class RequiresParameterInjection
