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
internal class BucketFillerTest(@Autowired val bucketFiller: BucketFiller) {

    private val expectedX = 4
    private val expectedY = 4
    private val expectedC = "c"
    private val canvas: Canvas = createDummyCanvas(10, 10)

    @Test
    fun validates() {
        val matchResult = bucketFiller.validates("${Command.L} $expectedX $expectedY $expectedC", canvas)

        assertThat(matchResult.groups["x"]!!.value.toInt()).isEqualTo(expectedX)
        assertThat(matchResult.groups["y"]!!.value.toInt()).isEqualTo(expectedY)
        assertThat(matchResult.groups["c"]!!.value).isEqualTo(expectedC)
    }

    @Test
    fun `validates with canvas not created should throw CanvasNotCreatedException`() {
        assertThrows(CanvasNotCreatedException::class.java) {
            bucketFiller.validates("${Command.L} $expectedX $expectedY $expectedC", Canvas())
        }
    }

    @Test
    fun `validates with wrong inputs should return WrongUserInputException`() {
        assertThrows(WrongUserInputException::class.java) {
            bucketFiller.validates("${Command.L}", canvas)
        }

        assertThrows(WrongUserInputException::class.java) {
            bucketFiller.validates("${Command.L} $expectedX", canvas)
        }

        assertThrows(WrongUserInputException::class.java) {
            bucketFiller.validates("${Command.L} $expectedX $expectedY", canvas)
        }

        assertThrows(WrongUserInputException::class.java) {
            bucketFiller.validates("${Command.L} $expectedC", canvas)
        }
    }

    @Test
    fun `validates with forbidden color x should return WrongUserInputException`() {
        assertThrows(WrongUserInputException::class.java) {
            bucketFiller.validates("${Command.L} $expectedX $expectedY x", canvas)
        }
    }

    @Test
    fun `validates with zero x or y should return WrongUserInputException`() {
        assertThrows(WrongUserInputException::class.java) {
            bucketFiller.validates("${Command.L} 0 $expectedY $expectedC", canvas)
        }

        assertThrows(WrongUserInputException::class.java) {
            bucketFiller.validates("${Command.L} $expectedX 0 $expectedC", canvas)
        }
    }

    @Test
    fun `validates with out of bound x or y should return WrongUserInputException`() {
        assertThrows(WrongUserInputException::class.java) {
            bucketFiller.validates("${Command.L} 11 $expectedY $expectedC", canvas)
        }

        assertThrows(WrongUserInputException::class.java) {
            bucketFiller.validates("${Command.L} $expectedX 11 $expectedC", canvas)
        }
    }

    @Test
    fun draws() {
        val newCanvas = Canvas(
                grids = arrayOf(
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', 'x', 'x', 'x', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', ' ', 'x', ' ', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', ' ', ' ', ' ', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', ' ', ' ', 'x', ' ', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', ' ', ' ', 'x', ' ', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', ' ', ' ', 'x', 'x', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', ' ', ' ', ' ', ' ', ' ', 'x', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', ' ', 'x', 'x', 'x', ' ', ' ', ' ')
                )
        )

        val expectedCanvas = Canvas(
                grids = arrayOf(
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', 'x', 'x', 'x', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', 'c', 'x', 'c', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', 'c', 'c', 'c', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', 'c', 'c', 'x', 'c', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', 'c', 'c', 'x', 'c', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', 'c', 'c', 'x', 'x', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', 'c', 'c', 'c', 'c', 'c', 'x', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', 'c', 'x', 'x', 'x', ' ', ' ', ' ')
                )
        )


        val matchResult = bucketFiller.validates("${Command.L} $expectedX $expectedY $expectedC", newCanvas)

        val actualCanvas = bucketFiller.draws(matchResult, newCanvas)

        assertThat(actualCanvas).isEqualTo(expectedCanvas)
    }

    @Test
    fun `draws fill two different color in one area`() {
        val newCanvas = Canvas(
                grids = arrayOf(
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', 'x', 'x', 'x', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', ' ', 'x', ' ', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', ' ', ' ', ' ', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', ' ', ' ', 'x', ' ', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', ' ', ' ', 'x', ' ', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', ' ', ' ', 'x', 'x', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', ' ', ' ', ' ', ' ', ' ', 'x', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', ' ', 'x', 'x', 'x', ' ', ' ', ' ')
                )
        )

        val expectedCanvas = Canvas(
                grids = arrayOf(
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', 'x', 'x', 'x', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', 'c', 'x', 'c', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', 'c', 'c', 'c', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', 'c', 'c', 'x', 'c', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', 'c', 'c', 'x', 'c', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', 'c', 'c', 'x', 'x', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', 'c', 'c', 'c', 'c', 'c', 'x', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', 'c', 'x', 'x', 'x', ' ', ' ', ' ')
                )
        )

        val expectedCanvasDifferentColor = Canvas(
                grids = arrayOf(
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', ' ', ' ', 'x', ' ', ' ', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', 'x', 'x', 'x', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', 'o', 'x', 'o', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', 'o', 'o', 'o', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', 'o', 'o', 'x', 'o', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', 'o', 'o', 'x', 'o', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', 'o', 'o', 'x', 'x', 'x', ' ', ' ', ' '),
                        charArrayOf(' ', 'x', 'o', 'o', 'o', 'o', 'o', 'x', ' ', ' '),
                        charArrayOf(' ', ' ', 'x', 'o', 'x', 'x', 'x', ' ', ' ', ' ')
                )
        )

        val matchResult = bucketFiller.validates("${Command.L} $expectedX $expectedY $expectedC", newCanvas)

        val actualCanvas = bucketFiller.draws(matchResult, newCanvas)

        assertThat(actualCanvas).isEqualTo(expectedCanvas)

        val matchResultDifferentColor = bucketFiller.validates("${Command.L} $expectedX $expectedY o", actualCanvas)
        val actualCanvasDifferentColor = bucketFiller.draws(matchResultDifferentColor, actualCanvas)

        assertThat(actualCanvasDifferentColor).isEqualTo(expectedCanvasDifferentColor)
    }
}