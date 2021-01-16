package tupperdate.web.utils.koin

import com.google.api.core.ApiFuture
import com.google.cloud.firestore.CollectionReference
import com.google.cloud.firestore.Firestore
import com.google.cloud.firestore.QueryDocumentSnapshot
import com.google.cloud.firestore.QuerySnapshot
import io.ktor.application.*
import io.ktor.server.testing.*
import org.koin.ktor.ext.Koin
import org.koin.ktor.ext.inject
import tupperdate.web.facade.chats.KoinModuleFacadeChat
import tupperdate.web.facade.profiles.KoinModuleFacadeProfile
import tupperdate.web.facade.recipes.KoinModuleFacadeRecipe
import tupperdate.web.installServer
import tupperdate.web.model.accounts.firestore.KoinModuleModelAuthFirebase
import tupperdate.web.model.chats.firestore.KoinModuleModelChatsFirestore
import tupperdate.web.model.impl.firestore.KoinModuleModelFirebase
import tupperdate.web.model.profiles.firestore.KoinModuleModelUsersFirestore
import tupperdate.web.model.recipes.firestore.KoinModuleModelRecipesFirestore


fun <R> withTupperdateTestApplication(engine: TestApplicationEngine.() -> R): R =
    withTestApplication {
        application.install(Koin) {
            modules(KoinModuleModelFirebase)
            modules(KoinModuleModelAuthFirebase)

            modules(KoinModuleModelUsersFirestore)
            modules(KoinModuleModelChatsFirestore)
            modules(KoinModuleModelRecipesFirestore)

            modules(KoinModuleFacadeAccountMock)
            modules(KoinModuleRepositoryPhoneMock)
            modules(KoinModuleNotificationMock)

            modules(KoinModuleFacadeProfile)
            modules(KoinModuleFacadeRecipe)
            modules(KoinModuleFacadeChat)
        }

        application.installServer()
        val firestore by application.inject<Firestore>()
        deleteCollection(firestore.collection("users"))
        deleteCollection(firestore.collection("recipes"))
        deleteCollection(firestore.collection("chats"))
        engine()
    }


/** Delete a collection in batches to avoid out-of-memory errors.
 * Batch size may be tuned based on document size (atmost 1MB) and application requirements.
 */
private fun deleteCollection(collection: CollectionReference, batchSize: Int = 100) {
    try {
        // retrieve a small batch of documents to avoid out-of-memory errors
        val future: ApiFuture<QuerySnapshot> = collection.limit(batchSize).get()
        var deleted = 0
        // future.get() blocks on document retrieval
        val documents: List<QueryDocumentSnapshot> = future.get().documents
        for (document in documents) {
            document.reference.delete()
            ++deleted
        }
        if (deleted >= batchSize) {
            // retrieve and delete another batch
            deleteCollection(collection, batchSize)
        }
    } catch (e: Exception) {
        System.err.println("Error deleting collection : " + e.message)
    }
}
