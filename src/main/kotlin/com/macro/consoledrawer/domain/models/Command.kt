package com.macro.consoledrawer.domain.models

import com.macro.consoledrawer.exception.WrongUserInputException

enum class Command {
    C, L, R, B, UNKNOWN;

    companion object {
        fun fromString(input: String): Command = try {
            valueOf(input)
        } catch (e: IllegalArgumentException) {
            throw WrongUserInputException(e.localizedMessage, e)
        }
    }
}