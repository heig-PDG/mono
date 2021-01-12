package tupperdate.web.model

class MissingDataException(override val message: String? = null): Exception()

class NotFoundException(override val message: String? = null) : Exception()
