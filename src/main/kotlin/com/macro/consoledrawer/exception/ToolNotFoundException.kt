package com.macro.consoledrawer.exception

class ToolNotFoundException : Exception {
    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)
}