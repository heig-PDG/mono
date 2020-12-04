package tupperdate.android.data

import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.dropbox.android.external.store4.StoreBuilder
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.auth.*
import io.ktor.client.features.json.*
import io.ktor.client.request.*
import io.ktor.http.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import tupperdate.android.data.auth.firebase
import tupperdate.android.data.room.TupperdateDatabase
import tupperdate.android.data.room.recipe.RecipeSourceOfTruth
import tupperdate.android.data.room.recipe.RecipeStackSourceOfTruth

/**
 * A single entry point that builds the different instances that will be needed to work with the
 * databases.
 *
 * TODO (alex) : switch to some dependency injection mechanism.
 */
@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
object Graph {

    lateinit var database: TupperdateDatabase
        private set

    lateinit var httpClient: HttpClient
        private set

    lateinit var scope: CoroutineScope
        private set

    val recipeStore by lazy {
        StoreBuilder.from(
            fetcher = RecipeFetcher.singleRecipeFetcher(httpClient),
            sourceOfTruth = RecipeSourceOfTruth(database.recipes()),
        ).scope(scope).build()
    }

    val recipeStackStore by lazy {
        StoreBuilder.from(
            fetcher = RecipeFetcher.allRecipesFetcher(httpClient),
            sourceOfTruth = RecipeStackSourceOfTruth(database.recipes())
        ).scope(scope).build()
    }

    /**
     * Provides the [Graph] with a specific [AppCompatActivity].
     */
    fun provide(context: AppCompatActivity) {

        database = Room.databaseBuilder(context, TupperdateDatabase::class.java, "db.sqlite")
            .fallbackToDestructiveMigration() // you would not want this in production
            .build()

        httpClient = HttpClient {
            install(Auth) { firebase(RealAuthenticationApi(context, FirebaseAuth.getInstance())) }
            install(JsonFeature)
            defaultRequest {
                // TODO : Use HTTPS.
                host = "api.tupperdate.me"
                port = 80
                contentType(ContentType.parse("application/json"))
            }
        }

        scope = context.lifecycleScope
    }
}
