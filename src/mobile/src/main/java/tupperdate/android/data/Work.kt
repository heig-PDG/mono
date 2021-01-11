@file:Suppress("FunctionName")

package tupperdate.android.data

import androidx.work.*
import java.util.concurrent.TimeUnit

/**
 * Builds a new sync request job, which is run in the background when the network connectivity
 * is guaranteed by the OS.
 */
inline fun <reified W : ListenableWorker> SyncRequestBuilder() =
    OneTimeWorkRequestBuilder<W>()
        .setBackoffCriteria(
            BackoffPolicy.LINEAR,
            OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
            TimeUnit.MILLISECONDS,
        )
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()
        )
