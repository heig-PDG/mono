package tupperdate.android.data

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.apache.commons.codec.binary.Base64
import java.io.ByteArrayOutputStream

// This could be adjusted depending on some device configuration. Ranges from 0-100
private const val CompressFactor = 10

/**
 * Reads a [Uri] into a base-64 encoded compressed JPEG image.
 *
 * @param contentResolver the compression factor to use for the transformation.
 */
@Suppress("BlockingMethodInNonBlockingContext")
suspend fun Uri.readFileAndCompressAsBase64(
    contentResolver: ContentResolver,
): String = withContext(Dispatchers.IO) {
    val array = ByteArrayOutputStream()
    contentResolver.openInputStream(this@readFileAndCompressAsBase64)
        .let(BitmapFactory::decodeStream)
        .compress(Bitmap.CompressFormat.JPEG, CompressFactor, array)
    // Manual string building, since old Android devices lack the encodeBase64String implementation
    // in their framework libs.
    String(array.toByteArray().let(Base64::encodeBase64))
}
