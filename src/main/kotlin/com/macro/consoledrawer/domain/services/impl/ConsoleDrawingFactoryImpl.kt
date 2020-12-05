package com.macro.consoledrawer.domain.services.impl

import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.domain.models.drawingtools.DrawingTool
import com.macro.consoledrawer.domain.services.DrawingFactory
import org.springframework.stereotype.Service

@Service
class ConsoleDrawingFactoryImpl(
        private val drawingTools: List<DrawingTool>
) : DrawingFactory() {
    override fun picks(command: Command): DrawingTool {
        return drawingTools
                .filter { it.type == command }
                .let { list ->
                    when {
                        list.isEmpty() -> throw IllegalStateException("[No such tool found] Illegal command '$command'")
                        list.size == 1 -> return@let list.first()
                        else -> throw IllegalStateException("[Multiple tools found] [command=$command] [tools = $list]")
                    }
                }
    }
}