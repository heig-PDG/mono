package tupperdate.android.data

import kotlinx.coroutines.channels.SendChannel

/**
 * Safely offers an item in a [SendChannel]. If the [SendChannel] is closed, no exception will be
 * thrown. This might be useful in scenarios where recomposition happens while an operation is
 * being performed.
 */
fun <T> SendChannel<T>.safeOffer(element: T): Boolean {
    return kotlin.runCatching { offer(element) }.getOrDefault(false)
}
