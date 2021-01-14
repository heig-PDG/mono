package tupperdate.web.model

sealed class Result<T> {
    data class Ok<T>(val result: T) : Result<T>()
    data class Forbidden<T>(val message: String? = null) : Result<T>()
    data class BadInput<T>(val message: String? = null) : Result<T>()
    data class NotFound<T>(val message: String? = null) : Result<T>()
    data class MissingData<T>(val message: String? = null) : Result<T>()
    data class BadServer<T>(val message: String? = null) : Result<T>()
}

inline fun <S, T> Result<S>.map(f: (S) -> T): Result<T> = when (this) {
    is Result.Ok -> Result.Ok(f(this.result))
    is Result.Forbidden -> Result.Forbidden()
    is Result.BadInput -> Result.BadInput(this.message)
    is Result.NotFound -> Result.NotFound(this.message)
    is Result.MissingData -> Result.MissingData(this.message)
    is Result.BadServer -> Result.BadServer(this.message)
}

inline fun <S, T> Result<S>.flatMap(f: (S) -> Result<T>): Result<T> = when (this) {
    is Result.Ok -> f(this.result)
    is Result.Forbidden -> Result.Forbidden()
    is Result.BadInput -> Result.BadInput(this.message)
    is Result.NotFound -> Result.NotFound(this.message)
    is Result.MissingData -> Result.MissingData(this.message)
    is Result.BadServer -> Result.BadServer(this.message)
}
