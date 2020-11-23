package tupperdate.web.exceptions

class UnauthorizedException(message: String? = "Unauthorized: the access token is missing or invalid") : Exception(message)

class BadRequestException(message: String? = "Bad Request") : Exception(message)

class ForbiddenException(message: String? = "Forbidden") : Exception(message)
