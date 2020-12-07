package com.macro.consoledrawer.interfaces

import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.domain.services.DrawingService
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils

@Service
@Profile("!test")
class CommandLineTaskExecutor(val drawingService: DrawingService) : CommandLineRunner {
    private val logger = LoggerFactory.getLogger(javaClass)
    private var canvas: Canvas = Canvas()

    companion object {
        private const val EXIT = "Q"
    }

    override fun run(vararg args: String?) {
        do {
            println("enter command: ")
            val userInput = readLine()
            println()
            if (userInput == null || StringUtils.isEmpty(userInput) || userInput == EXIT) {
                continue
            } else {
                runCatching {
                    canvas = drawingService.draws(userInput, canvas)
                    canvas.display()
                }.onFailure { logger.error(it.localizedMessage, it) }
            }
        } while (userInput != EXIT)
    }

    fun getCanvas() = canvas
}
