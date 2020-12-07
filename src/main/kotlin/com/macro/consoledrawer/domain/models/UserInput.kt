package com.macro.consoledrawer.domain.models

data class UserInput(
        val command: Command,
        val coordinate: Coordinate
) {
    data class Coordinate(
            val x1: Int,
            val y1: Int,
            val x2: Int?,
            val y2: Int?
    )
}