package com.macro.consoledrawer.exception

class CanvasNotCreatedException : Exception {
    constructor(message: String = "Canvas must be created before drawing") : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}