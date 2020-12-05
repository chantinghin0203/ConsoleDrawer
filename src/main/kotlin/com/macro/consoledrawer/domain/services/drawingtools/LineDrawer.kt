package com.macro.consoledrawer.domain.services.drawingtools

import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.exception.CanvasNotCreatedException
import com.macro.consoledrawer.exception.WrongUserInputException
import org.springframework.stereotype.Service

@Service
class LineDrawer : DrawingTool(Command.L) {
    override val regexMatcher = """[A-Z]\s(?<x1>\d+)\s(?<y1>\d+)\s(?<x2>\d+)\s(?<y2>\d+)""".toRegex()

    override fun validates(userInput: String, canvas: Canvas): MatchResult {
        if (canvas.getHeight() == 0 || canvas.getWeight() == 0) throw CanvasNotCreatedException()

        return regexMatcher.matchEntire(userInput)
                ?.apply {
                    val (x1, y1, x2, y2) = toCoordinate()

                    if (x1 != x2 && y1 != y2) TODO("Diagonal line is not supported")
                    if (x1 > canvas.getWeight() || x1 <= 0 || x2 > canvas.getWeight() || x2 <= 0) throw WrongUserInputException("Height is out of bound")
                    if (y1 > canvas.getHeight() || y1 <= 0 || y2 > canvas.getHeight() || y2 <= 0) throw WrongUserInputException("Height is out of bound")
                    if (x1 == x2 && y1 == y2) throw WrongUserInputException("Cannot create a line with two same points (x1, y1) and (x2, y2)")
                }
                ?: throw WrongUserInputException("Wrong input for drawing a line [$userInput]")

    }

    private fun MatchResult.toCoordinate() = listOf(groups["x1"]!!.value.toInt(), groups["y1"]!!.value.toInt(), groups["x2"]!!.value.toInt(), groups["y2"]!!.value.toInt())

    override fun draws(matchResult: MatchResult, canvas: Canvas): Canvas {
        val (x1, y1, x2, y2) = matchResult.toCoordinate()

        when {
            x1 == x2 -> for (j in if (y2 > y1) y1..y2 else y2..y1) {
                canvas.setGrid(x1, j)
            }
            y1 == y2 -> for (i in if (x2 > x1) x1..x2 else x2..x1) {
                canvas.setGrid(x1, i)
            }
            else -> {
                // TODO("Diagonal line is not supported")
            }
        }

        return canvas
    }
}