package com.macro.consoledrawer.domain.models

data class Canvas(
        val grids: Array<CharArray> = emptyArray()
) {
    fun isInBound(x: Int, y: Int) = y > 0 && y <= getHeight() && x > 0 && x <= getWidth()

    fun getHeight(): Int = grids.size

    fun getWidth(): Int = this.grids.first().size

    fun getGrid(width: Int, height: Int): Char = grids[height - 1][width - 1]

    fun setGrid(width: Int, height: Int, notation: Char = 'x') {
        grids[height - 1][width - 1] = notation
    }

    fun display() {
        println("---".repeat(getWidth()))

        for (row in grids) {
            println("|${row.joinToString("  ")}|")
        }
        println("---".repeat(getWidth()))
    }

    override fun equals(other: Any?): Boolean {
        if (javaClass != other?.javaClass) return false

        other as Canvas
        if (this.getWidth() != other.getWidth() || this.getHeight() != other.getHeight()) return false

        for (i in 1..this.getWidth()) {
            for (j in 1..this.getHeight()) {
                if (this.getGrid(i, j) != other.getGrid(i, j))
                    return false
            }
        }

        return true
    }

    override fun hashCode(): Int {
        return grids.contentDeepHashCode()
    }
}