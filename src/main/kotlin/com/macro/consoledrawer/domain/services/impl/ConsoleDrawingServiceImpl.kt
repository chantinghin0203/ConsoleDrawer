package com.macro.consoledrawer.domain.services.impl

import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.domain.services.DrawingFactory
import com.macro.consoledrawer.domain.services.DrawingService
import org.springframework.stereotype.Service

@Service
class ConsoleDrawingServiceImpl(
        private val drawingFactory: DrawingFactory
) : DrawingService {

    override fun draw(userInput: String, canvas: Canvas): Canvas {
        return drawingFactory.picks(Command.valueOf(userInput.first().toString()))
                .run {
                    val validate = validate(userInput, canvas)
                    return@run draw(validate, canvas)
                }
    }
}