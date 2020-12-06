package com.macro.consoledrawer.domain.services.drawingtools

import com.macro.consoledrawer.domain.models.Canvas
import com.macro.consoledrawer.domain.models.Command
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

    private val expectedX = 3
    private val expectedY = 5
    private val expectedC = "c"
    private val canvas: Canvas = Canvas(
            grids = Array(10) { CharArray(10) { ' ' } }
    )

    @Test
    fun validates() {
        val matchResult = bucketFiller.validates("${Command.L} $expectedX $expectedY $expectedC", canvas)

        assertThat(matchResult.groups["x"]!!.value.toInt()).isEqualTo(expectedX)
        assertThat(matchResult.groups["y"]!!.value.toInt()).isEqualTo(expectedY)
        assertThat(matchResult.groups["c"]!!.value).isEqualTo(expectedC)
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
        val matchResult = bucketFiller.validates("${Command.L} $expectedX $expectedY $expectedC", canvas)

        val newCanvas = bucketFiller.draws(matchResult, canvas)

        for (x in 1..newCanvas.getWidth()) {
            for (y in 1..newCanvas.getHeight()) {
                assertThat(newCanvas.getGrid(x, y)).isEqualTo('c')
            }
        }
    }
}