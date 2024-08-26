package ar.edu.unsam.phm.domain

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class ElementoYaExisteException(msg: String) : RuntimeException(msg)

@ResponseStatus(HttpStatus.NOT_FOUND)
class ElementoInexistenteException(msg: String) : RuntimeException(msg)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class PasswordUnmatchException(msg:String) :RuntimeException(msg)

@ResponseStatus(HttpStatus.BAD_REQUEST)
class Businessexception(msg:String): RuntimeException(msg)