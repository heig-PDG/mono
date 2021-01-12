package tupperdate.web.model.impl.firestore

import com.google.cloud.firestore.Firestore
import com.google.firebase.FirebaseApp
import com.google.firebase.cloud.FirestoreClient
import org.koin.dsl.module

val KoinModuleModelFirestore = module {
    factory<FirebaseApp> { FirebaseApp.getInstance() }
    factory<Firestore> { FirestoreClient.getFirestore(get()) }
}
