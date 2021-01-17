package tupperdate.android.data.features.profile.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.createDataStore
import kotlinx.coroutines.flow.map
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.profile.RestrictionsRepository

@InternalDataApi
class DatastoreRestrictionsRepository(
    context: Context,
) : RestrictionsRepository {

    private val datastore = context.createDataStore("restrictions")
    private val keyVegetarian = booleanPreferencesKey("vegetarian")
    private val keyAllergens = booleanPreferencesKey("allergens")

    override val warnIfNotVegetarian = datastore.data
        .map { it[keyVegetarian] ?: false }

    override val warnIfHasAllergens = datastore.data
        .map { it[keyAllergens] ?: false }

    override suspend fun toggleWarnVegetarian(newValue: Boolean) {
        datastore.edit { prefs ->
            prefs[keyVegetarian] = newValue
        }
    }

    override suspend fun toggleWarnHasAllergens(newValue: Boolean) {
        datastore.edit { prefs ->
            prefs[keyAllergens] = newValue
        }
    }
}
