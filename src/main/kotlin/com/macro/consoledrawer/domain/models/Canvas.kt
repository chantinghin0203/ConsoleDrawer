package com.macro.consoledrawer.domain.models

data class Canvas(
        val grids: Array<CharArray> = Array(0) { CharArray(0) { ' ' } }


) {
    fun getHeight(): Int = grids.size

    fun getWeight(): Int = this.grids.first().size

    fun getGrid(height: Int, weight: Int): Char = grids[height - 1][weight - 1]

    fun setGrid(weight: Int, height: Int, notation: Char = 'x') {
        grids[height - 1][weight - 1] = notation
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Canvas

        if (!grids.contentDeepEquals(other.grids)) return false

        return true
    }

    override fun hashCode(): Int {
        return grids.contentDeepHashCode()
    }

    fun display() {
        println("---".repeat(getWeight()))

        for (row in grids) {
            println("|${row.joinToString("  ")}|")
        }
        println("---".repeat(getWeight()))
    }
}