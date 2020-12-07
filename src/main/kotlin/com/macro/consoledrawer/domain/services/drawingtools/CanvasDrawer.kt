package com.macro.consoledrawer.domain.services.drawingtools

import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.exception.WrongUserInputException
import org.springframework.stereotype.Service

@Service
class CanvasDrawer : DrawingTool(Command.C) {
    companion object {
        private const val TAG_W = "w"
        private const val TAG_H = "h"
    }

    override val regexMatcher = """[A-Z]\s(?<$TAG_W>\d+)\s(?<$TAG_H>\d+)""".toRegex()

    override fun validates(userInput: String, canvas: Canvas): MatchResult {
        return regexMatcher.matchEntire(userInput)
                ?.apply {
                    val w = groups[TAG_W]!!.value.toInt()
                    val h = groups[TAG_H]!!.value.toInt()

                    if (w <= 0 || h <= 0)
                        throw WrongUserInputException("Width and Height must be positive number [$userInput]")
                }
                ?: throw WrongUserInputException("Wrong parameter for creating Canvas [$userInput]")
    }

    override fun draws(matchResult: MatchResult, canvas: Canvas): Canvas {
        return Canvas(
                grids = Array(
                        matchResult.groups[TAG_H]!!.value.toInt()
                ) {
                    CharArray(matchResult.groups[TAG_W]!!.value.toInt()) { ' ' }
                }
        )
    }
}