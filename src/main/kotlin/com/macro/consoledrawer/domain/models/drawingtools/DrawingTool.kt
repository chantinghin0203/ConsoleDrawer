package com.macro.consoledrawer.domain.models.drawingtools

import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.domain.models.Canvas


abstract class DrawingTool(val type: Command) {
    abstract val regexMatcher: Regex;
    abstract fun validate(userInput: String, canvas: Canvas): MatchResult
    abstract fun draw(matchResult: MatchResult, canvas: Canvas): Canvas
}