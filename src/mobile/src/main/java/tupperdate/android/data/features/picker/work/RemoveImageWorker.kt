package tupperdate.android.data.features.picker.work

import android.content.Context
import android.net.Uri
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import tupperdate.android.data.InternalDataApi
import java.io.File

/**
 * A [CoroutineWorker] that deletes the [File] provided at a certain [Uri]. Use the
 * [RemoveImageWorker.forUri] method to generate the appropriate input [Data].
 */
@InternalDataApi
class RemoveImageWorker(
    context: Context,
    params: WorkerParameters,
) : CoroutineWorker(context, params) {

    override suspend fun doWork(): Result {
        val path = inputData.asUri()?.path ?: return Result.failure()
        val file = File(path)
        try {
            file.delete()
        } finally {
            return Result.success()
        }
    }

    companion object {

        private const val ParameterUri = "uri"

        private fun Data.asUri(): Uri? {
            val text = this.getString(ParameterUri) ?: return null
            return Uri.parse(text)
        }

        fun forUri(uri: Uri): Data {
            return workDataOf(ParameterUri to uri.toString())
        }
    }
}
