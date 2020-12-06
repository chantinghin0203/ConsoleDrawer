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
        const val EMPTY_GRID = ' '
    }

    override val regexMatcher = """[A-Z]\s(?<x>\d+)\s(?<y>\d+)\s(?<c>[a-zA-Z])""".toRegex()

    override fun validates(userInput: String, canvas: Canvas): MatchResult {
        if (canvas.getHeight() == 0) throw CanvasNotCreatedException()

        return regexMatcher.matchEntire(userInput)
                ?.apply {
                    val (x, y) = toCoordinatePair()

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
        stack.push(matchResult.toCoordinatePair())

        while (stack.isNotEmpty()) {
            val size = stack.size
            for (i in 1..size) {
                val (x, y) = stack.pop()

                if (canvas.isInBound(x, y) && canvas.getGrid(x, y) == EMPTY_GRID) {
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

    private fun MatchResult.toCoordinatePair() = Pair(groups["x"]!!.value.toInt(), groups["y"]!!.value.toInt())
    private fun MatchResult.toColor() = groups["c"]!!.value.first()
}