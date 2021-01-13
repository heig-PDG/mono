package tupperdate.web.model

class UnauthorizedException(override val message: String? = null) : Exception()
class ForbiddenException(override val message: String? = null) : Exception()
class BadInputException(override val message: String? = null) : Exception()
class NotFoundException(override val message: String? = null) : Exception()
class MissingData(override val message: String? = null): Exception()
class BadServer(override val message: String? = null): Exception()
