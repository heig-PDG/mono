package tupperdate.android.data

import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow

/**
 * Safely offers an item in a [SendChannel]. If the [SendChannel] is closed, no exception will be
 * thrown. This might be useful in scenarios where recomposition happens while an operation is
 * being performed.
 */
fun <T> SendChannel<T>.safeOffer(element: T): Boolean {
    return kotlin.runCatching { offer(element) }.getOrDefault(false)
}

/**
 * Drops all the elements emitted by a flow that do not match an `is` predicate after at least one
 * element of the [Flow] has matched the predicate.
 *
 * @param T the type of the flow elements.
 * @param R the type of the elements we're interested in.
 */
inline fun <reified T, reified R> Flow<T>.dropAfterInstance(): Flow<T> = flow {
    var emitAll = true
    collect { elem ->
        if (emitAll || elem is R) {
            emit(elem)
        }
        emitAll = emitAll && elem !is R
    }
}
