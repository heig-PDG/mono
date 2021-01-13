package tupperdate.web.model

sealed class Result<T> {
    data class Ok<T>(val result: T) : Result<T>()
    data class Forbidden<T>(val error: Throwable) : Result<T>()
    data class BadInput<T>(val error: Throwable) : Result<T>()
    data class NotFound<T>(val error: Throwable) : Result<T>()
    data class MissingData<T>(val error: Throwable) : Result<T>()
    data class BadServer<T>(val error: Throwable) : Result<T>()
}
