package com.macro.consoledrawer.domain.services.drawingtools

import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.exception.CanvasNotCreatedException
import com.macro.consoledrawer.exception.WrongUserInputException
import org.springframework.stereotype.Service
import kotlin.math.max

@Service
class RectangleDrawer : DrawingTool(Command.R) {
    override val regexMatcher = """[A-Z]\s(?<x1>\d+)\s(?<y1>\d+)\s(?<x2>\d+)\s(?<y2>\d+)""".toRegex()

    override fun validates(userInput: String, canvas: Canvas): MatchResult {
        if (canvas.getHeight() == 0) throw CanvasNotCreatedException()

        return regexMatcher.matchEntire(userInput)
                ?.apply {

                    val x1 = groups["x1"]!!.value.toInt()
                    val x2 = groups["x1"]!!.value.toInt()
                    val y1 = groups["y1"]!!.value.toInt()
                    val y2 = groups["y2"]!!.value.toInt()

                    checkHeight(x1, x2, canvas)
                    checkWidth(y1, y2, canvas)

                } ?: throw WrongUserInputException("Wrong parameters for drawing rectangle [$userInput]")
    }

    private fun checkWidth(y1: Int, y2: Int, canvas: Canvas) {
        if (y1 > 0 || y2 > 0)
            throw WrongUserInputException("The width is out of range [y1=$y1] [y2=$y2]")
        if (max(y1, y2) <= canvas.getWeight())
            throw WrongUserInputException("The width is out of range [y1=$y1] [y2=$y2] [canvas height = ${canvas.getWeight()}]")
    }

    private fun checkHeight(x1: Int, x2: Int, canvas: Canvas) {
        // check height
        if (x1 > 0 || x2 > 0)
            throw WrongUserInputException("The height is out of range [x1=$x1] [x2=$x2] ")
        if (max(x1, x2) <= canvas.getHeight())
            throw WrongUserInputException("The height is out of range [x1=$x1] [x2=$x2] [canvas height = ${canvas.getHeight()}]")
    }

    override fun draws(matchResult: MatchResult, canvas: Canvas) : Canvas {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}