package tupperdate.android.data.features.recipe

import io.ktor.client.*
import io.ktor.client.request.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import tupperdate.android.data.features.recipe.room.NewRecipeDao
import tupperdate.android.data.features.recipe.room.asDTO

class NewRecipePusher(
    private val dao: NewRecipeDao,
    private val client: HttpClient,
) {

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    operator fun invoke(): Flow<Unit> = every(PushInterval)
        .flatMapLatest { dao.all() } // fetch the recipes
        .flatMapConcat { it.asFlow() } // map them to individual recipes
        .distinct() // TODO : support retries
        .map { recipe ->
            try {
                // TODO : Improve client-side image handling.
                // TODO : Compress images.
                client.post<Unit>("/recipes") {
                    body = recipe.asDTO()
                }
                dao.delete(recipe.identifier)
            } catch (problem: Throwable) {
                problem.printStackTrace()
            }
        }
}

private const val PushInterval = 10 * 1000L

private fun every(millis: Long): Flow<Long> = flow {
    var now = 0L
    while (true) {
        emit(now)
        delay(millis)
        now += millis
    }
}

/**
 * Removes duplicates items from a [Flow].
 */
private fun <T> Flow<T>.distinct(): Flow<T> = flow {
    val previous = mutableSetOf<T>()
    collect { item ->
        if (item !in previous) {
            previous += item
            emit(item)
        }
    }
}
