package com.macro.consoledrawer.domain.services.drawingtools

import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.exception.WrongUserInputException
import org.springframework.stereotype.Service

@Service
class CanvasDrawer : DrawingTool(Command.C) {
    override val regexMatcher = """[A-Z]\s(?<w>\d+)\s(?<h>\d+)""".toRegex()

    override fun validates(userInput: String, canvas: Canvas): MatchResult {
        return regexMatcher.matchEntire(userInput)
                ?.apply {
                    val w = groups["w"]!!.value.toInt()
                    val h = groups["h"]!!.value.toInt()

                    if (w <= 0 || h <= 0)
                        throw WrongUserInputException("Width and Height must be positive number [$userInput]")
                }
                ?: throw WrongUserInputException("Wrong parameter for creating Canvas [$userInput]")
    }

    override fun draws(matchResult: MatchResult, canvas: Canvas): Canvas {
        return canvas.copy(
                grids = Array(
                        matchResult.groups["h"]!!.value.toInt()
                ) {
                    CharArray(matchResult.groups["w"]!!.value.toInt())
                }
        )
    }
}