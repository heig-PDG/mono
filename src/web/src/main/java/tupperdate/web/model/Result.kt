package tupperdate.web.model

sealed class Result<T> {
    data class Ok<T>(val result: T) : Result<T>()
    class BadAuthentication<T>(val error: Throwable) : Result<T>()
    class BadInput<T>(val error: Throwable) : Result<T>()
    class BadServer<T>(val error: Throwable) : Result<T>()
    class NotFound<T>(val error: Throwable) : Result<T>()
}
