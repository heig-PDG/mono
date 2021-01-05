package tupperdate.android.data.features.auth.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import tupperdate.android.data.InternalDataApi

/**
 * Returns a [Flow] of the current [FirebaseUser], and emits one on every authentication change.
 */
@InternalDataApi
@OptIn(ExperimentalCoroutinesApi::class)
val FirebaseAuth.currentUserFlow: Flow<FirebaseUser?>
    get() = callbackFlow {
        val callback = FirebaseAuth.AuthStateListener { auth ->
            safeOffer(auth.currentUser)
        }
        this@currentUserFlow.addAuthStateListener(callback)
        awaitClose { this@currentUserFlow.removeAuthStateListener((callback)) }
    }

/**
 * Safely offers an item in a [SendChannel]. If the [SendChannel] is closed, no exception will be
 * thrown. This might be useful in scenarios where recomposition happens while an operation is
 * being performed.
 *
 * TODO : Make this API public.
 */
private fun <T> SendChannel<T>.safeOffer(element: T): Boolean {
    return kotlin.runCatching { offer(element) }.getOrDefault(false)
}
