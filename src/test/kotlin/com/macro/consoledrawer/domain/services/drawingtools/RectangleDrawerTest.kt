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
internal class RectangleDrawerTest(@Autowired val rectangleDrawer: RectangleDrawer) {
    private val canvas: Canvas = Canvas(
            grids = Array(10) { CharArray(10) { ' ' } }
    )

    private val expectedX1 = 5
    private val expectedY1 = 5
    private val expectedX2 = 1
    private val expectedY2 = 1

    @Test
    fun validates() {
        val matchResult = rectangleDrawer.validates("${Command.R} $expectedX1 $expectedY1 $expectedX2 $expectedY2", canvas)

        assertThat(matchResult.groups["x1"]!!.value.toInt()).isEqualTo(expectedX1)
        assertThat(matchResult.groups["y1"]!!.value.toInt()).isEqualTo(expectedY1)
        assertThat(matchResult.groups["x2"]!!.value.toInt()).isEqualTo(expectedX2)
        assertThat(matchResult.groups["y2"]!!.value.toInt()).isEqualTo(expectedY2)
    }

    @Test
    fun `validates with canvas not created should throw CanvasNotCreatedException`() {
        assertThrows(CanvasNotCreatedException::class.java) {
            rectangleDrawer.validates("${Command.R} $expectedX1 $expectedY1 $expectedX2 $expectedY2", Canvas())
        }
    }

    @Test
    fun `validates with wrong inputs should throw WrongUserInputException`() {
        assertThrows(WrongUserInputException::class.java) {
            rectangleDrawer.validates("${Command.R}", canvas)
        }

        assertThrows(WrongUserInputException::class.java) {
            rectangleDrawer.validates("${Command.R} $expectedX1 $expectedY1", canvas)
        }

        assertThrows(WrongUserInputException::class.java) {
            rectangleDrawer.validates("${Command.R} $expectedX1 $expectedY1 $expectedX2", canvas)
        }
    }

    @Test
    fun `validates with zero height or width should throw WrongUserInputException`() {
        assertThrows(WrongUserInputException::class.java) {
            rectangleDrawer.validates("${Command.R} 0 $expectedY1 $expectedX2 $expectedY2", canvas)
        }

        assertThrows(WrongUserInputException::class.java) {
            rectangleDrawer.validates("${Command.R} $expectedX1 0 $expectedX2 $expectedY2", canvas)
        }

        assertThrows(WrongUserInputException::class.java) {
            rectangleDrawer.validates("${Command.R} $expectedX1 $expectedY1 0 $expectedY2", canvas)
        }

        assertThrows(WrongUserInputException::class.java) {
            rectangleDrawer.validates("${Command.R} $expectedX1 $expectedY1 $expectedX2 0", canvas)
        }
    }


    @Test
    fun `validates with out of bound height or width should throw WrongUserInputException`() {
        assertThrows(WrongUserInputException::class.java) {
            rectangleDrawer.validates("${Command.R} 11 $expectedY1 $expectedX2 $expectedY2", canvas)
        }

        assertThrows(WrongUserInputException::class.java) {
            rectangleDrawer.validates("${Command.R} $expectedX1 11 $expectedX2 $expectedY2", canvas)
        }

        assertThrows(WrongUserInputException::class.java) {
            rectangleDrawer.validates("${Command.R} $expectedX1 $expectedY1 11 $expectedY2", canvas)
        }

        assertThrows(WrongUserInputException::class.java) {
            rectangleDrawer.validates("${Command.R} $expectedX1 $expectedY1 $expectedX2 11", canvas)
        }
    }

    @Test
    fun draws() {
        val expectedCanvas = Canvas(
                grids = arrayOf(
                        charArrayOf('x', 'x', 'x', 'x', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf('x', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf('x', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf('x', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf('x', 'x', 'x', 'x', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ')
                )
        )

        val matchResult = rectangleDrawer.validates("${Command.R} $expectedX1 $expectedY1 $expectedX2 $expectedY2", canvas)

        val newCanvas = rectangleDrawer.draws(matchResult, canvas)

        newCanvas.display()

        for (i in expectedX1..expectedX2) {
            assertThat(canvas.getGrid(i, expectedY1)).isEqualTo('x')
            assertThat(canvas.getGrid(i, expectedY2)).isEqualTo('x')
        }

        for (j in expectedY1..expectedY2) {
            assertThat(canvas.getGrid(expectedX1, j)).isEqualTo('x')
            assertThat(canvas.getGrid(expectedX2, j)).isEqualTo('x')
        }

        assertThat(newCanvas).isEqualTo(expectedCanvas)
    }
}