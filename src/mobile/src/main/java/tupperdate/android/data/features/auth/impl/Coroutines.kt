package tupperdate.android.data.features.auth.impl

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import tupperdate.android.data.InternalDataApi
import tupperdate.android.data.safeOffer

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
