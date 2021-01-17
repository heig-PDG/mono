package tupperdate.android.data.features.profile

import kotlinx.coroutines.flow.Flow

/**
 * A repository representing the user restrictions. Preferences are here to provided highlights to
 * recipes that match the preferences of the user, assuming all the
 */
interface RestrictionsRepository {

    /**
     * A [Flow] that returns true if the user wants warnings for non-vegetarian recipes.
     */
    val warnIfNotVegetarian: Flow<Boolean>

    /**
     * A [Flow] that retzrns true if the user wants warnings for recipes with allergens.
     */
    val warnIfHasAllergens: Flow<Boolean>

    suspend fun toggleWarnVegetarian(newValue: Boolean)
    suspend fun toggleWarnHasAllergens(newValue: Boolean)
}
