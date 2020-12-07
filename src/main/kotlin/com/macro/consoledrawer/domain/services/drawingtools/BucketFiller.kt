package com.macro.consoledrawer.domain.services.drawingtools

import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.exception.CanvasNotCreatedException
import com.macro.consoledrawer.exception.WrongUserInputException
import org.springframework.stereotype.Service
import java.util.*

@Service
class BucketFiller : DrawingTool(Command.B) {
    companion object {
        private const val TAG_X = "x"
        private const val TAG_Y = "y"
        private const val TAG_C = "c"
    }

    override val regexMatcher = """[A-Z]\s(?<$TAG_X>\d+)\s(?<$TAG_Y>\d+)\s(?<$TAG_C>[a-zA-Z])""".toRegex()

    override fun validates(userInput: String, canvas: Canvas): MatchResult {
        if (canvas.getHeight() == 0) throw CanvasNotCreatedException()

        return regexMatcher.matchEntire(userInput)
                ?.apply {
                    val (x, y) = toCoordinatePair()
                    val color = toColor()

                    if (color == 'x') {
                        throw WrongUserInputException("Cannot fill color x. It is reserved for drawing")
                    }
                    if (y > canvas.getHeight() || y <= 0)
                        throw WrongUserInputException("The y is out of range [y=$y]")
                    if (x > canvas.getWidth() || x <= 0)
                        throw WrongUserInputException("The y is out of range [x=$x]")


                }
                ?: throw WrongUserInputException("Wrong input for creating canvas [$userInput]")
    }

    override fun draws(matchResult: MatchResult, canvas: Canvas): Canvas {
        val color = matchResult.toColor()

        val stack = Stack<Pair<Int, Int>>()
        val initialCoordinate = matchResult.toCoordinatePair()
        val initialColor = canvas.getGrid(initialCoordinate.first, initialCoordinate.second)

        stack.push(initialCoordinate)
        while (stack.isNotEmpty()) {
            val size = stack.size
            for (i in 1..size) {
                val (x, y) = stack.pop()

                if (canvas.isInBound(x, y) && canvas.getGrid(x, y) == initialColor) {
                    stack.push(Pair(x + 1, y))
                    stack.push(Pair(x - 1, y))
                    stack.push(Pair(x, y + 1))
                    stack.push(Pair(x, y - 1))

                    canvas.setGrid(x, y, color)
                }
            }
        }
        return canvas
    }

    private fun MatchResult.toCoordinatePair() = Pair(groups[TAG_X]!!.value.toInt(), groups[TAG_Y]!!.value.toInt())
    private fun MatchResult.toColor() = groups[TAG_C]!!.value.first()
}