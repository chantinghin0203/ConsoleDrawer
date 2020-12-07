package com.macro.consoledrawer.domain.services.drawingtools

import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.exception.CanvasNotCreatedException
import com.macro.consoledrawer.exception.WrongUserInputException
import org.springframework.stereotype.Service

@Service
class LineDrawer : DrawingTool(Command.L) {
    companion object {
        private const val TAG_X1 = "x1"
        private const val TAG_Y1 = "y1"
        private const val TAG_X2 = "x2"
        private const val TAG_Y2 = "y2"
    }

    override val regexMatcher = """[A-Z]\s(?<$TAG_X1>\d+)\s(?<$TAG_Y1>\d+)\s(?<$TAG_X2>\d+)\s(?<$TAG_Y2>\d+)""".toRegex()

    override fun validates(userInput: String, canvas: Canvas): MatchResult {
        if (canvas.getHeight() == 0) throw CanvasNotCreatedException()

        return regexMatcher.matchEntire(userInput)
                ?.apply {
                    val (x1, y1, x2, y2) = toCoordinate()

                    if (x1 != x2 && y1 != y2) TODO("Diagonal line is not supported")
                    if (!canvas.isInBound(x1, y1) || !canvas.isInBound(x2, y2))
                        throw WrongUserInputException("Out of bound [maxHeight = ${canvas.getHeight()}] [maxWidth = ${canvas.getWidth()}]")
                    if (x1 == x2 && y1 == y2)
                        throw WrongUserInputException("Cannot create a line with two same points (x1, y1) and (x2, y2)")
                }
                ?: throw WrongUserInputException("Wrong input for drawing a line [$userInput]")

    }

    override fun draws(matchResult: MatchResult, canvas: Canvas): Canvas {
        return canvas.apply {
            matchResult.toCoordinate()
                    .let { (x1, y1, x2, y2) ->
                        when {
                            x1 == x2 -> for (j in if (y2 > y1) y1..y2 else y2..y1) {
                                this.setGrid(x1, j)
                            }
                            y1 == y2 -> for (i in if (x2 > x1) x1..x2 else x2..x1) {
                                this.setGrid(i, y1)
                            }
                            else -> {
                                // TODO("Diagonal line is not supported")
                            }
                        }
                    }
        }
    }

    private fun MatchResult.toCoordinate() = listOf(
            groups[TAG_X1]!!.value.toInt(),
            groups[TAG_Y1]!!.value.toInt(),
            groups[TAG_X2]!!.value.toInt(),
            groups[TAG_Y2]!!.value.toInt()
    )
}