package tupperdate.android.data.features.auth.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.dropbox.android.external.store4.StoreBuilder
import com.dropbox.android.external.store4.fresh
import com.google.firebase.auth.FirebaseAuth
import io.ktor.client.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.features.auth.firebase.currentUserFlow
import tupperdate.android.data.features.auth.store.ProfileFetcher
import tupperdate.android.data.features.auth.store.ProfileSourceOfTruth
import tupperdate.android.data.room.TupperdateDatabase

@InternalDataApi
@OptIn(
    KoinApiExtension::class,
    FlowPreview::class,
    ExperimentalCoroutinesApi::class,
)
class RefreshProfileWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val client by inject<HttpClient>()
    private val database by inject<TupperdateDatabase>()

    override suspend fun doWork(): Result {
        return try {
            val user = FirebaseAuth.getInstance().currentUserFlow.filterNotNull().first()
            val store = StoreBuilder.from(
                ProfileFetcher(client),
                ProfileSourceOfTruth(database.profiles())
            ).build()
            store.fresh(user.uid)
            Result.success()
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
            Result.retry()
        }
    }
}
