package com.macro.consoledrawer.domain.services.drawingtools

import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.domain.models.Command
import com.macro.consoledrawer.exception.CanvasNotCreatedException
import com.macro.consoledrawer.exception.WrongUserInputException
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
internal class CanvasDrawerTest(@Autowired val canvasDrawer: CanvasDrawer) {
    private val expectedW = 5
    private val expectedH = 5

    private val canvas: Canvas = Canvas(
            grids = Array(10) { CharArray(10) { ' ' } }
    )

    @Test
    fun validates() {
        val matchResult = canvasDrawer.validates("${Command.C} $expectedW $expectedH", canvas)

        assertThat(matchResult.groups["w"]!!.value.toInt()).isEqualTo(expectedW)
        assertThat(matchResult.groups["h"]!!.value.toInt()).isEqualTo(expectedH)
    }

    @Test
    fun `validates with no canvas created should throw CanvasNotCreatedException`() {
        val canvas = Canvas()

        assertThrows(CanvasNotCreatedException::class.java) {
            canvasDrawer.validates("${Command.C} $expectedW $expectedH", canvas)
        }
    }


    @Test
    fun `validates with zero height or weight should throw WrongUserInputException`() {
        assertThrows(WrongUserInputException::class.java) {
            canvasDrawer.validates("${Command.C} 0 $expectedH", canvas)
        }

        assertThrows(WrongUserInputException::class.java) {
            canvasDrawer.validates("${Command.C} $expectedW 0", canvas)
        }
    }


    @Test
    fun draws() {
        val matchResult = canvasDrawer.validates("${Command.C} $expectedW $expectedH", canvas)
        val newCanvas = canvasDrawer.draws(matchResult, canvas)

        assertThat(newCanvas.getHeight()).isEqualTo(expectedH)
        assertThat(newCanvas.getWeight()).isEqualTo(expectedW)
    }
}