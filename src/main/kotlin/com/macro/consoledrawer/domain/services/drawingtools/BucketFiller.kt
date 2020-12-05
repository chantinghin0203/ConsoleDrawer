package com.macro.consoledrawer.domain.services.drawingtools

import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.exception.CanvasNotCreatedException
import com.macro.consoledrawer.exception.WrongUserInputException
import org.springframework.stereotype.Service

@Service
class BucketFiller : DrawingTool(Command.B) {
    override val regexMatcher = """[A-Z]\s(?<x>\d+)\s(?<y>\d+)\s(?<c>[a-zA-Z])""".toRegex()

    override fun validates(userInput: String, canvas: Canvas): MatchResult {
        if (canvas.getHeight() == 0) throw CanvasNotCreatedException()



        return regexMatcher.matchEntire(userInput)
                ?.apply {
                    val x = groups["x"]!!.value.toInt()
                    val y = groups["y"]!!.value.toInt()
                    val c = groups["c"]!!.value

                    if (y > canvas.getHeight() || y <= 0)
                        throw WrongUserInputException("The y is out of range [y=$y]")
                    if (x > canvas.getWeight() || x <= 0)
                        throw WrongUserInputException("The y is out of range [x=$x]")


                }
                ?: throw WrongUserInputException("Wrong input for creating canvas [$userInput]")
    }

    override fun draws(matchResult: MatchResult, canvas: Canvas) : Canvas {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}