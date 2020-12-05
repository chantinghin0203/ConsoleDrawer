package com.macro.consoledrawer.domain.services.drawingtools

import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.domain.models.Canvas


abstract class DrawingTool(val type: Command) {
    abstract val regexMatcher: Regex;
    abstract fun validates(userInput: String, canvas: Canvas): MatchResult
    abstract fun draws(matchResult: MatchResult, canvas: Canvas): Canvas
}