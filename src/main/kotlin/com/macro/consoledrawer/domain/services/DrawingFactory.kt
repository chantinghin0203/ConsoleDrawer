package com.macro.consoledrawer.domain.services

import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.domain.models.drawingtools.DrawingTool

abstract class DrawingFactory {
    abstract fun picks(command: Command): DrawingTool
}