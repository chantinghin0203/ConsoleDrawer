package com.macro.consoledrawer.domain.models.drawingtools

import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.exception.CanvasNotCreatedException
import com.macro.consoledrawer.exception.WrongUserInputException
import org.springframework.stereotype.Service

@Service
class LineDrawer : DrawingTool(Command.L) {
    override val regexMatcher = """[A-Z]\s(?<x1>\d+)\s(?<y1>\d+)\s(?<x2>\d+)\s(?<y2>\d+)""".toRegex()

    override fun validate(userInput: String, canvas: Canvas): MatchResult {
        if (canvas.getHeight() == 0) throw CanvasNotCreatedException()

        return regexMatcher.matchEntire(userInput)
                ?.apply {
                    if (groups["x1"] == groups["x2"] || groups["y1"] == groups["y2"]) throw WrongUserInputException("Diagonal line is not supported")
                }
                ?: throw WrongUserInputException("Wrong input for drawing a line [$userInput]")

    }

    override fun draw(userInput: MatchResult, canvas: Canvas): Canvas {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}