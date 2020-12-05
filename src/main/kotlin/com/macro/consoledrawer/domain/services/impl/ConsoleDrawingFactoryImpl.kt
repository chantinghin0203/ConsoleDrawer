package com.macro.consoledrawer.domain.services.impl

import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.domain.services.drawingtools.DrawingTool
import com.macro.consoledrawer.domain.services.DrawingFactory
import com.macro.consoledrawer.exception.ToolNotFoundException
import org.springframework.stereotype.Service

@Service
class ConsoleDrawingFactoryImpl(
        private val drawingTools: List<DrawingTool>
) : DrawingFactory() {
    override fun picks(command: Command): DrawingTool {
        return drawingTools
                .filter { it.type == command }
                .let {
                    when {
                        it.isEmpty() -> throw ToolNotFoundException("[No such tool found] Illegal command '$command'")
                        it.size == 1 -> return@let it.first()
                        else -> throw IllegalStateException("[Multiple tools found] [command=$command] [tools = $it]")
                    }
                }
    }
}