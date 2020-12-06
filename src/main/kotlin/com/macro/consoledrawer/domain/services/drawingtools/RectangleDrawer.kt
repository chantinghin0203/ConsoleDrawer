package com.macro.consoledrawer.domain.services.drawingtools

import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.exception.CanvasNotCreatedException
import com.macro.consoledrawer.exception.WrongUserInputException
import org.springframework.stereotype.Service

@Service
class RectangleDrawer : DrawingTool(Command.R) {
    override val regexMatcher = """[A-Z]\s(?<x1>\d+)\s(?<y1>\d+)\s(?<x2>\d+)\s(?<y2>\d+)""".toRegex()

    override fun validates(userInput: String, canvas: Canvas): MatchResult {
        if (canvas.getHeight() == 0) throw CanvasNotCreatedException()

        return regexMatcher.matchEntire(userInput)
                ?.apply {

                    val (x1, y1, x2, y2) = toCoordinate()

                    if (!canvas.isInBound(x1, y1) || !canvas.isInBound(x2, y2))
                        throw WrongUserInputException("The width is out of range [maxHeight=${canvas.getHeight()}] [maxWidth=${canvas.getWidth()}]")
                    if (x1 == x2 && y1 == y2)
                        throw WrongUserInputException("Cannot create a rectangle with two same points [x1=$x1] [y1=$y1] [x2=$x2] [y2=$y2]")

                } ?: throw WrongUserInputException("Wrong parameters for drawing rectangle [$userInput]")
    }

    override fun draws(matchResult: MatchResult, canvas: Canvas): Canvas {
        val (x1, y1, x2, y2) = matchResult.toCoordinate()

        for (i in if (x2 > x1) x1..x2 else y2..y1) {
            canvas.setGrid(i, y1)
            canvas.setGrid(i, y2)
        }

        for (j in if (y2 > y1) y1..y2 else y2..y1) {
            canvas.setGrid(x1, j)
            canvas.setGrid(x2, j)
        }

        return canvas
    }

    private fun MatchResult.toCoordinate() = listOf(
            groups["x1"]!!.value.toInt(),
            groups["y1"]!!.value.toInt(),
            groups["x2"]!!.value.toInt(),
            groups["y2"]!!.value.toInt()
    )
}