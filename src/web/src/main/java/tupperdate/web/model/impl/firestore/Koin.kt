package tupperdate.web.model.impl.firestore

import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.cloud.FirestoreClient
import com.google.firebase.cloud.StorageClient
import com.google.firebase.messaging.FirebaseMessaging
import org.koin.dsl.module
import tupperdate.web.legacy.util.initialiseApp

val KoinModuleModelFirebase = module {
    single<FirebaseApp> { initialiseApp() }
    single<Firestore> { FirestoreClient.getFirestore(get()) }
    single<StorageClient> { StorageClient.getInstance(get()) }
    single<FirebaseAuth> { FirebaseAuth.getInstance(get()) }
    single<FirebaseMessaging> { FirebaseMessaging.getInstance(get()) }
}
