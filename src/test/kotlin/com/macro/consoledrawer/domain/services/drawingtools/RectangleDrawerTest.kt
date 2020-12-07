package com.macro.consoledrawer.domain.services.drawingtools

import CanvasHelper.Companion.createDummyCanvas
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
    private val canvas: Canvas = createDummyCanvas(10, 10)

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
    fun `validates with point at zero should throw WrongUserInputException`() {
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
    fun `validates with zero height or width should throw WrongUserInputException`() {
        assertThrows(WrongUserInputException::class.java) {
            rectangleDrawer.validates("${Command.R} 10 $expectedY1 10 $expectedY2", canvas)
        }

        assertThrows(WrongUserInputException::class.java) {
            rectangleDrawer.validates("${Command.R} $expectedX1 10 $expectedX2 10", canvas)
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
    fun `draws with one point at top left and one point at bottom right`() {
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

        val dummyCanvas = createDummyCanvas(10, 10)

        val matchResult = rectangleDrawer.validates("${Command.R} $expectedX1 $expectedY1 $expectedX2 $expectedY2", dummyCanvas)
        val newDummyCanvas = rectangleDrawer.draws(matchResult, dummyCanvas)
        assertThat(newDummyCanvas).isEqualTo(expectedCanvas)

        // swap two points
        val newDummyCanvasSwapped = createDummyCanvas(10, 10)
        val matchResultSwapped = rectangleDrawer.validates("${Command.R} $expectedX2 $expectedY2 $expectedX1 $expectedY1", newDummyCanvasSwapped)
        val newCanvasSwapped = rectangleDrawer.draws(matchResultSwapped, newDummyCanvasSwapped)
        assertThat(newCanvasSwapped).isEqualTo(expectedCanvas)
    }

    @Test
    fun `draws with one point at top right and one point at bottom left`() {
        val expectedX1 = 5
        val expectedY1 = 1
        val expectedX2 = 1
        val expectedY2 = 5
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

        val dummyCanvas = createDummyCanvas(10, 10)

        val matchResult = rectangleDrawer.validates("${Command.R} $expectedX1 $expectedY1 $expectedX2 $expectedY2", dummyCanvas)
        val newDummyCanvas = rectangleDrawer.draws(matchResult, dummyCanvas)
        assertThat(newDummyCanvas).isEqualTo(expectedCanvas)

        // swap two points
        val newDummyCanvasSwapped = createDummyCanvas(10, 10)
        val matchResultSwapped = rectangleDrawer.validates("${Command.R} $expectedX2 $expectedY2 $expectedX1 $expectedY1", newDummyCanvasSwapped)
        val newCanvasSwapped = rectangleDrawer.draws(matchResultSwapped, newDummyCanvasSwapped)
        assertThat(newCanvasSwapped).isEqualTo(expectedCanvas)
    }
}