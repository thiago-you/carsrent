package br.pucpr.carsrent.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException(
    message: String = HttpStatus.BAD_REQUEST.reasonPhrase,
    cause: Throwable? = null
): IllegalArgumentException(message, cause)