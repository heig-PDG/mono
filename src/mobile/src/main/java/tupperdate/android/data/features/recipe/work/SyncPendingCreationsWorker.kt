package tupperdate.android.data.features.recipe.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import io.ktor.client.*
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.room.TupperdateDatabase

@InternalDataApi
@OptIn(KoinApiExtension::class)
class SyncPendingCreationsWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params), KoinComponent {

    private val client: HttpClient by inject()
    private val database: TupperdateDatabase by inject()

    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }
}
